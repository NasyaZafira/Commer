package com.commer.app.ui.shop

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.commer.app.R
import com.commer.app.base.BaseActivity
import com.commer.app.data.model.remote.shop.Data
import com.commer.app.databinding.ActivityShopBinding
import com.commer.app.ui.CustomLoadingDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShopActivity : BaseActivity() {

    private lateinit var binding: ActivityShopBinding
    private val viewModel: ShopViewModel by viewModels()
    private var adapterUsr =
        ShopAdapter(mutableListOf(), onDetail = { data -> intentToDetail(data) })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Commit_Home)
        binding = ActivityShopBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingUI = CustomLoadingDialog(this)

        lifecycleScope.launch {
            viewModel.listProduct()
        }

        setupObserver()
    }


    private fun intentToDetail(content: Data) {
        val i = Intent(this, DetailShopActivity::class.java)
        i.putExtra("product", content.id.toLong())
        startActivity(i)
        finish()
    }


    override fun onStart() {
        super.onStart()
        binding.swipeRefresh.setOnRefreshListener {
            lifecycleScope.launch {
                viewModel.listProduct()
            }
            binding.swipeRefresh.isRefreshing = false
        }
    }

    override fun setupObserver() {
        viewModel.message.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
        viewModel.statusCode.observe(this) {
            if (it != 200) {
                binding.imgHaveNoPost.visibility = View.VISIBLE
                binding.txtImgHome.visibility = View.VISIBLE
                binding.rvProductuser.visibility = View.INVISIBLE
            }
        }

        viewModel.loading.observe(this) {
            if (it) {
                showLoading()
            } else {
                hideLoading()
            }
        }
        viewModel.listProduct.observe(this) { response ->
            when (response.data.isNotEmpty()) {
                true -> {
                    binding.imgHaveNoPost.visibility = View.INVISIBLE
                    binding.txtImgHome.visibility = View.INVISIBLE
                    binding.rvProductuser.visibility = View.VISIBLE
                    adapterUsr.product.clear()
                    adapterUsr.product.addAll(response.data)
                    adapterUsr.notifyDataSetChanged()
                    binding.rvProductuser.apply {
                        layoutManager = GridLayoutManager(context, 2)
                        adapter = adapterUsr
                    }
                }
                false -> {
                    binding.imgHaveNoPost.visibility = View.VISIBLE
                    binding.txtImgHome.visibility = View.VISIBLE
                    binding.rvProductuser.visibility = View.INVISIBLE
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}