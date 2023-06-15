package com.commer.app.ui.settings.shop

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.commer.app.R
import com.commer.app.base.BaseActivity
import com.commer.app.databinding.ActivityShopHistoryBinding
import com.commer.app.ui.CustomLoadingDialog
import com.commer.app.ui.settings.transactions.TransactionHistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint

class ShopHistoryActivity : BaseActivity() {

    private lateinit var binding: ActivityShopHistoryBinding
    private val viewModel: TransactionHistoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Commit_Home)
        binding = ActivityShopHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingUI = CustomLoadingDialog(this)

        binding.imgBtnBack.setOnClickListener {
            onBackPressed()
        }

        lifecycleScope.launch {
            viewModel.getTransactionShopServer()
        }
        setupObserver()
    }

    override fun onStart() {
        super.onStart()
        binding.swipeRefresh.setOnRefreshListener {
            lifecycleScope.launch {
                viewModel.getTransactionShopServer()
            }
            binding.swipeRefresh.isRefreshing = false
        }
    }

    override fun setupObserver() {
        viewModel.getTransactionShopResponse.observe(this) {
            if (it.data.isNotEmpty()) {
                binding.imgHaveNoTransaction.visibility = View.GONE
                binding.recyclerTransactionShop.visibility = View.VISIBLE
            }
            binding.recyclerTransactionShop.apply {
                Log.e("TAG", "setupObserver: " + "recycler show")
                layoutManager = LinearLayoutManager(
                    this@ShopHistoryActivity,
                    RecyclerView.VERTICAL,
                    true
                )
                adapter = TransactionShopAdapter(it.data)
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