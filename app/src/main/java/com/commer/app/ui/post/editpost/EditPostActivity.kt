package com.commer.app.ui.post.editpost

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.commer.app.R
import com.commer.app.base.BaseActivity
import com.commer.app.data.local.CommerSharedPref
import com.commer.app.data.model.remote.post.editpost.EditPostResponse
import com.commer.app.databinding.ActivityEditPostBinding
import com.commer.app.ui.CustomLoadingDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditPostActivity : BaseActivity() {

    private lateinit var binding: ActivityEditPostBinding
    private val viewModel: EditPostViewModel by viewModels()
    private var selectedTags = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val getSimpler = intent.getStringExtra("simpler")
        if (getSimpler != null) {
            setTheme(R.style.Theme_Commit_Simpler_Post)
        } else {
            setTheme(R.style.Theme_Commit_Home)
        }
        binding = ActivityEditPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingUI = CustomLoadingDialog(this)

        binding.imgBtnBack.setOnClickListener {
            onBackPressed()
        }

        binding.imgBtnMedia.setOnClickListener {
            AlertOnlyTextDialog().show(supportFragmentManager, "AlertOnlyTextDialog")
        }

        binding.imgBtnVideo.setOnClickListener {
            AlertOnlyTextDialog().show(supportFragmentManager, "AlertOnlyTextDialog")
        }

        val getData = intent.getParcelableExtra<EditPostResponse>("getData")!!
        selectedTags = getData.postTags.joinToString(",")

        binding.imgBtnTag.setOnClickListener {
            TopicsPostAtEditPost().apply {
                val bundle = Bundle().apply { putString("tags", selectedTags) }
                arguments = bundle
                show(supportFragmentManager, "TopicsPostAtEditPost")
            }
        }

        binding.editTxtPost.setText(getData.postDesc)
        binding.txtCount.text = getData.postDesc.length.toString()

        binding.editTxtPost.doOnTextChanged { text, _, _, _ ->
            binding.txtCount.text = text?.length.toString()
            binding.btnPost.isEnabled = selectedTags.isNotEmpty() && text.toString().trim().isNotEmpty()
        }

        binding.btnPost.setOnClickListener { editPost() }

        setupObserver()
    }

    override fun setupObserver() {
        viewModel.editPostResponse.observe(this) {
            if (it.status == "200") {
                onBackPressed()
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

    private fun editPost() {
        val getData = intent.getParcelableExtra<EditPostResponse>("getData")!!
        lifecycleScope.launch {
            val text = binding.editTxtPost.text.toString().trim()
            val userStatus = CommerSharedPref.userStatus
            if (userStatus == "User" || userStatus == "Subscribed") {
                viewModel.editPostToServer(getData.idPost, false, selectedTags, text)
            } else {
                StatusPostAtEditPost().apply {
                    val bundle = Bundle().apply { putBoolean("status", getData.postStatus) }
                    arguments = bundle
                    show(supportFragmentManager, "StatusPostAtEditPost")
                }
            }
        }
    }

    fun showSelectedTags(tags: String) {
        selectedTags = tags
        val text = binding.editTxtPost.text.toString().trim()
        binding.btnPost.isEnabled = tags.isNotEmpty() && text.isNotEmpty()
    }

    fun showPostStatus(status: Boolean) {
        val getData = intent.getParcelableExtra<EditPostResponse>("getData")!!
        lifecycleScope.launch {
            val text = binding.editTxtPost.text.toString().trim()
            viewModel.editPostToServer(getData.idPost, status, selectedTags, text)
        }
    }
}