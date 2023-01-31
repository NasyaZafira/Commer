package com.commer.app.ui.settings.profile

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.anilokcun.uwmediapicker.UwMediaPicker
import com.bumptech.glide.Glide
import com.commer.app.CommerApp
import com.commer.app.R
import com.commer.app.base.BaseActivity
import com.commer.app.data.model.remote.user.detail.DetailProfile
import com.commer.app.databinding.ActivityUpdateProfileBinding
import com.commer.app.ui.CustomLoadingDialog
import com.commer.app.ui.CustomNotification
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class UpdateProfileActivity : BaseActivity() {

    private lateinit var binding: ActivityUpdateProfileBinding
    private val viewModel: UpdateProfileViewModel by viewModels()
    private var textFullName = ""
    private val errorBio = "Bio can't start with a space"
    private var selectedFiles = mutableListOf<File>()
    private val File.size get() = if (!exists()) 0.0 else length().toDouble()
    private val File.sizeInKb get() = size / 1024
    private val File.sizeInMb get() = sizeInKb / 1024

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Commit_Home)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingUI = CustomLoadingDialog(this)

        binding.imgBtnBack.setOnClickListener {
            onBackPressed()
        }

        binding.imgBtnGetPhoto.setOnClickListener {
            requestAccessForFile()
        }

        val getData = intent.getParcelableExtra<DetailProfile>("getData")!!
        binding.editFullName.setText(getData.fullname)
        textFullName = getData.fullname
        binding.editBio.setText(getData.bio)
        if (getData.profilePic != null) {
            Glide.with(this)
                .load(getData.profilePic)
                .into(binding.imgProfile)
        }
        if (getData.bio != null) {
            binding.txtCount.text = getData.bio.length.toString()
        }

        binding.editFullName.doOnTextChanged { text, _, _, _ ->
            val errorName = "The name can't be empty or contain numbers and symbols"
            if (
                !(text!!.matches("^(?![\\s.]+\$)[a-zA-Z\\s.]{1,100}\$".toRegex()) &&
                        text.isNotEmpty())
            ) {
                binding.txtErrorHandlingFullName.text = errorName
                binding.txtErrorHandlingFullName.visibility = View.VISIBLE
                binding.btnSave.isEnabled = false
            } else {
                binding.txtErrorHandlingFullName.visibility = View.GONE
                if (text.toString().trim().isNotEmpty()) {
                    textFullName = text.toString().trim()
                    binding.btnSave.isEnabled =
                        textFullName.trim().isNotEmpty()
                }
            }
        }

        binding.editBio.doOnTextChanged { text, _, _, _ ->
            binding.txtCount.text = text?.length.toString()
            val bio = text.toString()
            if (bio.startsWith(" ") || bio.startsWith("\n")) {
                binding.txtErrorHandlingBio.visibility = View.VISIBLE
                binding.txtErrorHandlingBio.text = errorBio
                binding.btnSave.isEnabled = false
            } else {
                if (binding.txtErrorHandlingFullName.visibility == View.GONE) {
                    binding.txtErrorHandlingBio.visibility = View.GONE
                    binding.btnSave.isEnabled =
                        textFullName.trim().isNotEmpty()
                }
            }
        }

        binding.btnSave.setOnClickListener { updateProfile() }

        setupObserver()
    }

    override fun setupObserver() {
        val notificationUI = CustomNotification(this, CommerApp.notificationChannelId)
        val notificationManager = NotificationManagerCompat.from(this)
        val progressMax = 100
        val progressCurrent = 0
        viewModel.updateProfileResponse.observe(this) {
            if (it.status == "200") {
                showMessageToast("Success")
                showNotification("Completed")
            } else {
                showNotification("Failed")
            }
        }
        viewModel.notificationError.observe(this) {
            if (it) showNotification("Failed")
        }
        viewModel.notification.observe(this) {
            if (it) {
                notificationUI.setProgress(progressMax, progressCurrent, true)
                notificationManager.notify(CommerApp.notificationChannelId, 1, notificationUI.build())
            }
        }
        viewModel.message.observe(this) {
            showMessageToast(it)
        }
        viewModel.loading.observe(this) {
            if (it) showLoading() else hideLoading()
        }
    }

    private fun updateProfile() {
        lifecycleScope.launch {
            binding.btnSave.isEnabled = false
            val bio = binding.editBio.text.toString()
            if (!bio.startsWith(" ") || !bio.startsWith("\n")) {
                if (selectedFiles.isNotEmpty())
                    viewModel.updateProfileToServer(
                        binding.editFullName.text.toString().trim(),
                        bio.trim(),
                        selectedFiles
                    )
                else
                    viewModel.updateProfileToServer(
                        binding.editFullName.text.toString(),
                        binding.editBio.text.toString(),
                        null)
            } else {
                binding.txtErrorHandlingBio.visibility = View.VISIBLE
                binding.txtErrorHandlingBio.text = errorBio
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

    private fun selectFileForUpload() {
        UwMediaPicker
            .with(this)
            .setGalleryMode(UwMediaPicker.GalleryMode.ImageGallery)
            .setGridColumnCount(3)
            .setMaxSelectableMediaCount(1)
            .setLightStatusBar(true)
            .enableImageCompression(true)
            .setCompressFormat(Bitmap.CompressFormat.JPEG)
            .setCompressionQuality(50)
            .setCompressedFileDestinationPath(filesDir.path)
            .launch { f ->
                selectedFiles.clear()
                f?.let { files ->
                    files.forEach {
                        val file = File(it.mediaPath)
                        if (file.sizeInMb <= 5.0) {
                            Glide.with(this)
                                .load(file.path)
                                .into(binding.imgProfile)
                            selectedFiles.add(file)
                            binding.btnSave.isEnabled = true
                        } else {
                            showMessageToast("Maximum selected photo must be less than 5 MB")
                        }
                    }
                }
            }
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
                showMessageToast("The app needs your permission")
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}