package com.commer.app.ui.simpler

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.anilokcun.uwmediapicker.UwMediaPicker
import com.commer.app.base.BaseActivity
import com.commer.app.data.local.CommerSharedPref
import com.commer.app.databinding.ActivitySimplerPaymentUploadBinding
import com.commer.app.ui.CustomLoadingDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.math.RoundingMode

@AndroidEntryPoint
class SimplerPaymentUploadActivity : BaseActivity() {

    private lateinit var binding: ActivitySimplerPaymentUploadBinding
    private val viewModel: SimplerPaymentViewModel by viewModels()
    private var selectedFiles = mutableListOf<File>()

    private val File.size get() = if (!exists()) 0.0 else length().toDouble()
    private val File.sizeInKb get() = size / 1024

    private var plan = ""
    private var currentTransactionDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySimplerPaymentUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingUI = CustomLoadingDialog(this)

        binding.imgArrowBack.setOnClickListener { onBackPressed() }

        intent.extras?.let {
            val userID = CommerSharedPref.userId
            val currentDateTime = it.getString("currentDateTime")
            plan = it.getString("plan").toString()
            currentTransactionDate = it.getString("currentTransactionDate").toString()
            val idTransaction = "CM$userID$currentDateTime"
            binding.txtTransactionID.text = idTransaction
        }

        binding.btnCopy.setOnClickListener {
            val accountNumber = binding.txtBankAccountNumber.text.toString()
            val clipData = ClipData.newPlainText("text", accountNumber)
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT)
                .show()
        }

        binding.layoutImage.setOnClickListener {
            requestAccessForFile()
        }

        binding.imgCrossmark.setOnClickListener {
            selectedFiles.clear()
            binding.layoutImageNotUploaded.visibility = View.VISIBLE
            binding.layoutImageUploaded.visibility = View.GONE
            binding.btnNextPayment.isEnabled = false
        }

        binding.btnNextPayment.setOnClickListener {
            createSimplerTransaction()
        }

        setupObserver()
    }

    private fun createSimplerTransaction() {
        val transactionID = binding.txtTransactionID.text.toString().trim()
        lifecycleScope.launch {
            viewModel.postSimplerPaymentToServer(plan, transactionID, selectedFiles)
        }
    }

    override fun setupObserver() {
        viewModel.message.observe(this) {
            showMessageToast(it)
        }
        viewModel.loading.observe(this) {
            if (it) showLoading() else hideLoading()
        }
        viewModel.simplerPaymentResponse.observe(this) {
            if (it.status == "200") {
                val transactionID = binding.txtTransactionID.text.toString().trim()
                showMessageToast("Success create transaction for $transactionID")
                val i = Intent(this, SimplerPaymentLoadingActivity::class.java)
                i.putExtra("fromPayment", "fromPayment")
                startActivity(i)
                finish()
            } else {
                showMessageToast(it.message)
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

    private fun selectFileForUpload() {
        UwMediaPicker
            .with(this)
            .setGalleryMode(UwMediaPicker.GalleryMode.ImageGallery)
            .setGridColumnCount(3)
            .setMaxSelectableMediaCount(1)
            .setLightStatusBar(true)
            .enableImageCompression(true)
            .setCompressionQuality(50)
            .setCompressedFileDestinationPath(this@SimplerPaymentUploadActivity.filesDir.path)
            .setCancelCallback {
                selectedFiles.clear()
            }
            .launch { f ->
                binding.layoutImageNotUploaded.visibility = View.GONE
                binding.layoutImageUploaded.visibility = View.VISIBLE
                binding.btnNextPayment.isEnabled = true
                f?.let { files ->
                    selectedFiles.clear()
                    selectedFiles.addAll(files.map {
                        File(it.mediaPath)
                    })
                    files.forEach {
                        val file = File(it.mediaPath)
                        val fileSize =
                            file.sizeInKb.toBigDecimal().setScale(3, RoundingMode.UP).toDouble()
                                .toString() + " kb"
                        binding.txtFileName.text = file.name
                        binding.txtFileSize.text = fileSize
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