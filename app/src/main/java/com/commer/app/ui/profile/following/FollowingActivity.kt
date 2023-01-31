package com.commer.app.ui.profile.following

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.commer.app.base.BaseActivity
import com.commer.app.databinding.ActivityFollowingBinding
import com.commer.app.ui.CustomLoadingDialog
import com.commer.app.ui.profile.other.ProfileOtherActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FollowingActivity : BaseActivity() {

    private lateinit var binding: ActivityFollowingBinding
    private val viewModel: FollowingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFollowingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingUI = CustomLoadingDialog(this)

        binding.imgBtnBack.setOnClickListener {
            onBackPressed()
        }

        val getIdUser = intent.getIntExtra("getIdUser", 0)
        lifecycleScope.launch {
            viewModel.getFollowingUserFromServer(getIdUser)
        }

        setupObserver()
    }

    override fun onStart() {
        super.onStart()
        val getIdUser = intent.getIntExtra("getIdUser", 0)
        binding.swipeRefresh.setOnRefreshListener {
            lifecycleScope.launch {
                viewModel.getFollowingUserFromServer(getIdUser)
            }
            binding.swipeRefresh.isRefreshing = false
        }
    }

    override fun setupObserver() {
        viewModel.followingResponse.observe(this) {
            binding.recyclerFollowing.apply {
                layoutManager = LinearLayoutManager(
                    this@FollowingActivity,
                    RecyclerView.VERTICAL,
                    true
                )
                adapter = FollowingAdapter(
                    it, object : FollowingAdapter.ButtonOnClickListener {
                        override fun setButtonFollow(userId: Int) {
                            performButtonFollow(userId)
                        }

                        override fun setButtonUnFollow(userId: Int) {
                            performButtonUnfollow(userId)
                        }
                    },
                    onItemClick = { item ->
                        val i = Intent(this@FollowingActivity, ProfileOtherActivity::class.java)
                        i.putExtra("getIdUser", item.id)
                        startActivity(i)
                    }
                )
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