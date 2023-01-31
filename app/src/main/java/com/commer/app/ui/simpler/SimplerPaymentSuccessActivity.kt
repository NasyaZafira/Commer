package com.commer.app.ui.simpler

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.commer.app.databinding.ActivitySimplerPaymentSuccessBinding
import com.commer.app.ui.login.LoginActivity
import com.commer.app.ui.main.MainActivity

class SimplerPaymentSuccessActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySimplerPaymentSuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySimplerPaymentSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.view2.visibility = View.GONE
        Handler(Looper.getMainLooper()).postDelayed({
            binding.view1.visibility = View.GONE
            binding.view2.visibility = View.VISIBLE
        }, 2170)

        intent.extras?.let {
            val price = it.getString("price")
            val planDetails = it.getString("plan details")
            val forMonths = it.getString("for x month")
            val date = it.getString("date")
            binding.txtPrice.text = price
            binding.txtMonthsPlan.text = planDetails
            binding.txtForXMonths.text = forMonths
            binding.txtTransactionDateValue.text = date
        }

        binding.btnReloginNow.setOnClickListener {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }

        binding.btnReturnToHomepage.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)

        }
    }
}