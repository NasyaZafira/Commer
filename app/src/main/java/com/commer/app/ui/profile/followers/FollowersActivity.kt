package com.commer.app.ui.profile.followers

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.commer.app.base.BaseActivity
import com.commer.app.databinding.ActivityFollowersBinding
import com.commer.app.ui.CustomLoadingDialog
import com.commer.app.ui.profile.other.ProfileOtherActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FollowersActivity : BaseActivity() {

    private lateinit var binding: ActivityFollowersBinding
    private val viewModel: FollowersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFollowersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingUI = CustomLoadingDialog(this)

        binding.imgBtnBack.setOnClickListener {
            onBackPressed()
        }

        val getIdUser = intent.getIntExtra("getIdUser", 0)
        lifecycleScope.launch {
            viewModel.getFollowersUserFromServer(getIdUser)
        }

        setupObserver()
    }

    override fun onStart() {
        super.onStart()
        val getIdUser = intent.getIntExtra("getIdUser", 0)
        binding.swipeRefresh.setOnRefreshListener {
            lifecycleScope.launch {
                viewModel.getFollowersUserFromServer(getIdUser)
            }
            binding.swipeRefresh.isRefreshing = false
        }
    }

    override fun setupObserver() {
        viewModel.followersResponse.observe(this) {
            binding.recyclerFollowers.apply {
                layoutManager = LinearLayoutManager(
                    this@FollowersActivity,
                    RecyclerView.VERTICAL,
                    true
                )
                adapter = FollowersAdapter(it, object : FollowersAdapter.ButtonOnClickListener {
                    override fun setButtonFollow(userId: Int) {
                        performButtonFollow(userId)
                    }

                    override fun setButtonUnFollow(userId: Int) {
                        performButtonUnfollow(userId)
                    }
                },
                    onItemClick = { item ->
                        val i = Intent(this@FollowersActivity, ProfileOtherActivity::class.java)
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