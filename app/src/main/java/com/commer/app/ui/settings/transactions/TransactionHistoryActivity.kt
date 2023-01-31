package com.commer.app.ui.settings.transactions

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.commer.app.R
import com.commer.app.base.BaseActivity
import com.commer.app.databinding.ActivityTransactionHistoryBinding
import com.commer.app.ui.CustomLoadingDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TransactionHistoryActivity : BaseActivity() {

    private lateinit var binding: ActivityTransactionHistoryBinding
    private val viewModel: TransactionHistoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Commit_Home)
        binding = ActivityTransactionHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingUI = CustomLoadingDialog(this)

        binding.imgBtnBack.setOnClickListener {
            onBackPressed()
        }

        lifecycleScope.launch {
            viewModel.getTransactionHistoryFromServer()
        }

        setupObserver()
    }

    override fun onStart() {
        super.onStart()
        binding.swipeRefresh.setOnRefreshListener {
            lifecycleScope.launch {
                viewModel.getTransactionHistoryFromServer()
            }
            binding.swipeRefresh.isRefreshing = false
        }
    }

    override fun setupObserver() {
        viewModel.getTransactionHistoryResponse.observe(this) {
            if (it.status == "200") {
                if (it.data.isNotEmpty()) {
                    binding.imgHaveNoTransaction.visibility = View.GONE
                    binding.recyclerTransaction.visibility = View.VISIBLE
                }
                binding.recyclerTransaction.apply {
                    layoutManager = LinearLayoutManager(
                        this@TransactionHistoryActivity,
                        RecyclerView.VERTICAL,
                        true
                    )
                    adapter = TransactionHistoryAdapter(it.data)
                }
            }
        }
        viewModel.message.observe(this) {
            showMessageToast(it)
        }
        viewModel.loading.observe(this) {
            if (it) showLoading() else hideLoading()
        }
    }
}