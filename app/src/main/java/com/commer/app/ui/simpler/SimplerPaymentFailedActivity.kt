package com.commer.app.ui.simpler

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.commer.app.databinding.ActivitySimplerPaymentFailedBinding
import com.commer.app.ui.main.MainActivity

class SimplerPaymentFailedActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySimplerPaymentFailedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySimplerPaymentFailedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            binding.view1.visibility = View.GONE
            binding.view2.visibility = View.VISIBLE
        }, 2000)

        binding.btnCreateNewTransaction.setOnClickListener {
            val i = Intent(this, SubscriptionPlanActivity::class.java)
            startActivity(i)
        }

        binding.btnReturnToHomepage.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
        binding.txtBitly.setOnClickListener {
            val url = "https://bit.ly/41unJLx"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
    }
}