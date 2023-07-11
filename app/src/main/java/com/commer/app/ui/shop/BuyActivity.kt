package com.commer.app.ui.shop

import android.content.Intent
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.commer.app.R
import com.commer.app.base.BaseActivity
import com.commer.app.databinding.ActivityBuyBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class BuyActivity : BaseActivity() {

    private lateinit    var binding     : ActivityBuyBinding
    private             val viewModel   : ShopViewModel by viewModels()
    private lateinit    var url_r       : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Commit_Home)
        binding = ActivityBuyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.icBack.setOnClickListener {
            val i = Intent(this, ShopActivity::class.java)
            startActivity(i)
            finish()
        }

        getId()

    }

    private fun getId() {
        val id = intent.getLongExtra("product", 0)
        intentExtras(id.toInt())
    }

    private fun intentExtras(id : Int) {
        lifecycleScope.launch {
            viewModel.buyProduct(id)
        }
        setupObserver()
    }


    override fun setupObserver() {

        viewModel.buy.observe(this){
            url_r                           = it.data.payment_url

            val webView                     = binding.webView
            val webSetting                  = webView.settings

            webSetting.javaScriptEnabled    = true
            webSetting.allowContentAccess   = true
            webSetting.domStorageEnabled    = true
            webSetting.useWideViewPort      = true

            webView.setWebViewClient(WebViewClient())
            webView.loadUrl(url_r)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val i = Intent(this, ShopActivity::class.java)
        startActivity(i)
        finish()
    }

}