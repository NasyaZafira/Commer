package com.commer.app.ui.post.newpost

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anilokcun.uwmediapicker.UwMediaPicker
import com.commer.app.CommerApp
import com.commer.app.base.BaseActivity
import com.commer.app.data.local.CommerSharedPref
import com.commer.app.databinding.ActivityNewPostBinding
import com.commer.app.ui.CustomLoadingDialog
import com.commer.app.ui.CustomNotification
import com.commer.app.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class NewPostActivity : BaseActivity() {

    private lateinit var binding: ActivityNewPostBinding
    private val viewModel: NewPostViewModel by viewModels()
    private var selectedTags = ""
    private var selectedFiles = mutableListOf<File>()
    private val File.size get() = if (!exists()) 0.0 else length().toDouble()
    private val File.sizeInKb get() = size / 1024
    private val File.sizeInMb get() = sizeInKb / 1024
    private val filesAdapter = FilesAdapter(mutableListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadingUI = CustomLoadingDialog(this)

        binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgBtnBack.setOnClickListener {
            onBackPressed()
        }

        binding.imgBtnMedia.setOnClickListener {
            requestAccessForFile()
        }

        binding.imgBtnVideo.setOnClickListener {
            requestAccessForVideo()
        }

        binding.imgBtnTag.setOnClickListener {
            BottomSheetTopicsPost().apply {
                val bundle = Bundle().apply { putString("tags", selectedTags) }
                arguments = bundle
                show(supportFragmentManager, "BottomSheetTopicsPost")
            }
        }

        binding.editTxtPost.doOnTextChanged { text, _, _, _ ->
            binding.txtCount.text = text?.length.toString()
            binding.btnPost.isEnabled = selectedTags.isNotEmpty() || text.toString().trim().isNotEmpty()
        }
        binding.btnPost.setOnClickListener { createNewPost() }

        binding.recyclerFiles.apply {
            layoutManager = LinearLayoutManager(
                this@NewPostActivity,
                RecyclerView.HORIZONTAL,
                false
            )
            adapter = filesAdapter
        }

        binding.txtClearMedia.setOnClickListener {
            filesAdapter.selectedFiles.clear()
            binding.txtClearMedia.visibility = View.GONE
            binding.recyclerFiles.visibility = View.GONE
            selectedFiles.clear()
            binding.imgBtnVideo.isEnabled = true
            binding.imgBtnMedia.isEnabled = true
        }

        setupObserver()
    }

    private fun createNewPost() {
        lifecycleScope.launch {
            val text = binding.editTxtPost.text.toString().trim()
            val userStatus = CommerSharedPref.userStatus
            if (selectedTags.isNotEmpty()) {
                if (text.isNotEmpty()) {
                    if (userStatus == "User" || userStatus == "Subscribed") {
                        if (selectedFiles.isNotEmpty())
                            viewModel.postNewPostToServer(false, selectedTags, text, selectedFiles)
                        else
                            viewModel.postNewPostToServer(false, selectedTags, text, null)
                    } else {
                        BottomSheetPostStatus().show(supportFragmentManager, "BottomSheetPostStatus")
                    }
                }
            } else {
                AlertTopicsDialog().show(supportFragmentManager, "AlertTopicsDialog")
            }
        }
    }

    fun showSelectedTags(tags: String) {
        selectedTags = tags
        val text = binding.editTxtPost.text.toString().trim()
        binding.btnPost.isEnabled = tags.isNotEmpty() || text.isNotEmpty()
    }

    fun showPostStatus(status: Boolean) {
        lifecycleScope.launch {
            val text = binding.editTxtPost.text.toString().trim()
            if (selectedFiles.isNotEmpty())
                viewModel.postNewPostToServer(status, selectedTags, text, selectedFiles)
            else
                viewModel.postNewPostToServer(status, selectedTags, text, null)
        }
    }

    override fun setupObserver() {
        val notificationUI = CustomNotification(this, CommerApp.notificationChannelId)
        val notificationManager = NotificationManagerCompat.from(this)
        val progressMax = 100
        val progressCurrent = 0
        viewModel.statusCode.observe(this) {
            if (it != 200) {
                showNotification("Failed")
            }
        }
        viewModel.notification.observe(this) {
            if (it) {
                notificationUI.setProgress(progressMax, progressCurrent, true)
                notificationManager.notify(CommerApp.notificationChannelId, 1, notificationUI.build())
            }
        }
        viewModel.notificationError.observe(this) {
            if (it)
                showNotification("Failed")
        }
        viewModel.message.observe(this) {
            showMessageToast(it)
        }
        viewModel.loading.observe(this) {
            if (it) showLoading() else hideLoading()
        }
        viewModel.newPostResponse.observe(this) {
            if (it.status == "200") {
                showNotification("Completed")
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                finish()
            } else {
                showMessageToast(it.message)
                showNotification("Failed")
            }
        }
    }

    private fun showNotification(text: String) {
        val notificationUI = CustomNotification(this, CommerApp.notificationChannelId)
        val notificationManager = NotificationManagerCompat.from(this)
        notificationUI.setContentText(text)
        notificationUI.setProgress(0, 0, false)
        notificationManager.notify(CommerApp.notificationChannelId, 1, notificationUI.build())
    }

    private fun requestAccessForFile() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                777
            )
        } else {
            selectFileForUpload()
        }
    }

    private fun requestAccessForVideo() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                777
            )
        } else {
            selectVideoForUpload()
        }
    }

    private fun selectFileForUpload() {
        UwMediaPicker
            .with(this)
            .setGalleryMode(UwMediaPicker.GalleryMode.ImageGallery)
            .setGridColumnCount(3)
            .setMaxSelectableMediaCount(5)
            .setLightStatusBar(true)
            .enableImageCompression(true)
            .setCompressFormat(Bitmap.CompressFormat.JPEG)
            .setCompressionQuality(50)
            .setCompressedFileDestinationPath(this@NewPostActivity.filesDir.path)
            .launch { f ->
                binding.imgBtnVideo.isEnabled = false
                selectedFiles.clear()
                filesAdapter.selectedFiles.clear()
                filesAdapter.notifyDataSetChanged()
                f?.let { files ->
                    files.forEach {
                        val file = File(it.mediaPath)
                        if (file.sizeInMb <= 5.0) {
                            selectedFiles.add(file)
                            binding.recyclerFiles.visibility = View.VISIBLE
                            binding.txtClearMedia.visibility = View.VISIBLE
                            filesAdapter.selectedFiles.add(file)
                        } else {
                            showMessageToast("Maximum selected photo must be less than 5 MB")
                        }
                    }
                }
            }
    }

    private fun selectVideoForUpload() {
        UwMediaPicker
            .with(this)
            .setGalleryMode(UwMediaPicker.GalleryMode.VideoGallery)
            .setGridColumnCount(3)
            .setMaxSelectableMediaCount(1)
            .setLightStatusBar(true)
            .setCompressionQuality(50)
            .setCompressedFileDestinationPath(this@NewPostActivity.filesDir.path)
            .launch { f ->
                binding.imgBtnMedia.isEnabled = false
                selectedFiles.clear()
                filesAdapter.selectedFiles.clear()
                filesAdapter.notifyDataSetChanged()
                f?.let { files ->
                    files.forEach {
                        val file = File(it.mediaPath)
                        if (file.sizeInMb <= 100.0) {
                            selectedFiles.add(file)
                            binding.recyclerFiles.visibility = View.VISIBLE
                            binding.txtClearMedia.visibility = View.VISIBLE
                            filesAdapter.selectedFiles.add(file)
                        } else {
                            showMessageToast("Maximum selected video must be less than 100 MB")
                        }
                    }
                }
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 777) {
            if (
                grantResults.isNotEmpty() && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED
            ) {
                selectFileForUpload()
            } else {
                Toast.makeText(
                    this,
                    "The app needs your permission",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}