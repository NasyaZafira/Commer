package com.commer.app.ui.report

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.commer.app.R
import com.commer.app.base.BaseActivity
import com.commer.app.databinding.ActivityReportBinding
import com.commer.app.ui.CustomLoadingDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReportActivity : BaseActivity() {

    private lateinit var binding: ActivityReportBinding
    private val viewModel: ReportViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Commit_Home)
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingUI = CustomLoadingDialog(this)

        binding.imgBtnBack.setOnClickListener {
            onBackPressed()
        }

        val getIdPost = intent.getIntExtra("getIdPost", 0)
        val getIdComment = intent.getIntExtra("getIdComment", 0)
        val getIdUser = intent.getIntExtra("getIdUser", 0)

        binding.radioButtonReport.setOnCheckedChangeListener { _, checkedId ->
            val radio = findViewById<RadioButton>(checkedId)
            if (radio.text == "Other") {
                binding.inputOtherReport.visibility = View.VISIBLE
                binding.constraintCounter.visibility = View.VISIBLE
                binding.editOtherReport.doOnTextChanged { text, _, _, _ ->
                    binding.txtCount.text = text?.length.toString()
                    binding.btnSendReport.isEnabled = radio.text.isNotEmpty() && text.toString().isNotEmpty()
                    if (getIdPost != 0) {
                        binding.btnSendReport.setOnClickListener {
                            reportPost(getIdPost, text.toString())
                        }
                    }
                    if (getIdComment != 0) {
                        binding.btnSendReport.setOnClickListener {
                            reportComment(getIdComment, text.toString())
                        }
                    }
                    if (getIdUser != 0) {
                        binding.btnSendReport.setOnClickListener {
                            reportUser(getIdUser, text.toString())
                        }
                    }
                }
            } else {
                binding.inputOtherReport.visibility = View.INVISIBLE
                binding.constraintCounter.visibility = View.INVISIBLE
                binding.btnSendReport.isEnabled = radio.text.isNotEmpty()
                if (getIdPost != 0) {
                    binding.btnSendReport.setOnClickListener {
                        reportPost(getIdPost, radio.text.toString())
                    }
                }
                if (getIdComment != 0) {
                    binding.btnSendReport.setOnClickListener {
                        reportComment(getIdComment, radio.text.toString())
                    }
                }
                if (getIdUser != 0) {
                    binding.btnSendReport.setOnClickListener {
                        reportUser(getIdUser, radio.text.toString())
                    }
                }
            }
        }

        setupObserver()
    }

    override fun setupObserver() {
        viewModel.reportUserResponse.observe(this) {
            if (it.status == "200") {
                intentReportSuccess()
            } else {
                showMessageToast(it.message)
            }
        }
        viewModel.reportPostResponse.observe(this) {
            if (it.status == "200") {
                intentReportSuccess()
            } else {
                showMessageToast(it.message)
            }
        }
        viewModel.reportCommentResponse.observe(this) {
            if (it.status == "200") {
                intentReportSuccess()
            } else {
                showMessageToast(it.message)
            }
        }
        viewModel.message.observe(this) {
            showMessageToast(it)
        }
        viewModel.loading.observe(this) {
            if (it) showLoading() else hideLoading()
        }
    }

    private fun intentReportSuccess() {
        val i = Intent(this, ReportSuccessActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun reportPost(idPost: Int, reason: String) {
        lifecycleScope.launch {
            viewModel.reportPostToServer(idPost, reason)
        }
    }

    private fun reportComment(idComment: Int, reason: String) {
        lifecycleScope.launch {
            viewModel.reportCommentToServer(idComment, reason)
        }
    }

    private fun reportUser(idUser: Int, reason: String) {
        lifecycleScope.launch {
            viewModel.reportUserToServer(idUser, reason)
        }
    }
}