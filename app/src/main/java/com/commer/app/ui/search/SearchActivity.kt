package com.commer.app.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.commer.app.R
import com.commer.app.base.BaseActivity
import com.commer.app.databinding.ActivitySearchBinding
import com.commer.app.ui.CustomLoadingDialog
import com.commer.app.ui.profile.other.ProfileOtherActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchActivity : BaseActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingUI = CustomLoadingDialog(this)

        binding.imgBtnBack.setOnClickListener {
            onBackPressed()
        }

        binding.editSearch.setOnEditorActionListener { textView, i, _ ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                val txtResult = "\"${textView.text.trim()}\""
                binding.txtSearchResult.text = txtResult
                lifecycleScope.launch {
                    viewModel.getUsersFromServer(textView.text.toString().trim())
                }
            }
            true
        }

        setupObserver()
    }

    override fun setupObserver() {
        viewModel.statusCode.observe(this) {
            if (it == 200) {
                binding.linearTxtSearch.visibility = View.VISIBLE
                binding.recyclerUser.visibility = View.VISIBLE
                binding.txtSearch.text = getString(R.string.search_result_for)
                binding.linearTxtSearch.gravity = Gravity.NO_GRAVITY
            } else {
                binding.linearTxtSearch.visibility = View.VISIBLE
                binding.recyclerUser.visibility = View.INVISIBLE
                val text = "No result for"
                binding.txtSearch.text = text
                binding.linearTxtSearch.gravity = Gravity.CENTER
            }
        }
        viewModel.getAllUsersResponse.observe(this) { response ->
            if (response.data.isNotEmpty()) {
                binding.linearTxtSearch.visibility = View.VISIBLE
                binding.recyclerUser.visibility = View.VISIBLE
            }
            binding.recyclerUser.apply {
                layoutManager = LinearLayoutManager(
                    this@SearchActivity,
                    RecyclerView.VERTICAL,
                    true
                )
                adapter =
                    SearchAdapter(
                        response.data,
                        onItemClick = { profile ->
                            val i = Intent(this@SearchActivity, ProfileOtherActivity::class.java)
                            i.putExtra("getIdUser", profile.id.toLong())
                            startActivity(i)
                        },
                        object : SearchAdapter.ButtonOnClickListener {
                        override fun setButtonFollow(userId: Int) {
                            performButtonFollow(userId)
                        }

                        override fun setButtonUnFollow(userId: Int) {
                            performButtonUnfollow(userId)
                        }
                    })
            }
        }
        viewModel.message.observe(this) {
            showMessageToast(it)
        }
        viewModel.loading.observe(this) {
            if (it) showLoading() else hideLoading()
        }
    }

    private fun performButtonFollow(userId: Int) {
        lifecycleScope.launch {
            viewModel.postFollowUserToServer(userId)
        }
    }

    private fun performButtonUnfollow(userId: Int) {
        lifecycleScope.launch {
            viewModel.postUnFollowUserToServer(userId)
        }
    }
}