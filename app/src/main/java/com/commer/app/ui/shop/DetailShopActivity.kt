package com.commer.app.ui.shop

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.commer.app.R
import com.commer.app.base.BaseActivity
import com.commer.app.data.model.remote.shop.buy.Data
import com.commer.app.databinding.ActivityContactUsBinding
import com.commer.app.databinding.ActivityDetailShopBinding
import com.commer.app.ui.CustomLoadingDialog
import com.commer.app.ui.settings.contact.ContactUsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailShopActivity : BaseActivity() {

    private lateinit    var binding: ActivityDetailShopBinding
    private             val viewModel: ShopViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Commit_Home)
        binding = ActivityDetailShopBinding.inflate(layoutInflater)
        setContentView(binding.root)

        runApi()
        binding.icBack.setOnClickListener {
            onBackPressed()
        }
        binding.contactAdmin.setOnClickListener {
            val i = Intent(this, ContactUsActivity::class.java)
            startActivity(i)
        }
    }

    private fun runApi() {
        val id = intent.getLongExtra("product", 0)
        intent(id.toInt())
    }


    private fun intent(id: Int){
        lifecycleScope.launch {
            viewModel.getDetailProduct(id)
            setupObserver()
        }
    }

    private fun intentToPay(content: Data){
        val url = content.payment_url
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    override fun setupObserver() {
        loadingUI = CustomLoadingDialog(this)
        viewModel.loading.observe(this) {
            if (it) showLoading() else hideLoading()
        }

        viewModel.detailProduct.observe(this){ response ->
        Glide.with(binding.ivPoster.context)
                .load(response.data.images )
                .error(R.drawable._995)
                .into(binding.ivPoster)

            binding.tvTitle.text = response.data.name
            binding.txtPrice.text = response.data.price
            binding.tvDesc.text = response.data.desc

            binding.btnOrderNow.setOnClickListener {
                val i = Intent(this, BuyActivity::class.java)
                i.putExtra("product", response.data.id.toLong())
                startActivity(i)
                finish()
            }
        }
    }

}