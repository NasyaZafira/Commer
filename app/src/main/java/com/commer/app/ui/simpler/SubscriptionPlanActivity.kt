package com.commer.app.ui.simpler

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.commer.app.databinding.ActivitySubscriptionPlanBinding

class SubscriptionPlanActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubscriptionPlanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySubscriptionPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgArrowBack.setOnClickListener { onBackPressed() }

        val i = Intent(this, SimplerPaymentDetailsActivity::class.java)

        binding.card1months.setOnClickListener {
            i.putExtra("xMonths", " 1 month")
            i.putExtra("months", "1")
            i.putExtra("price", "IDR 10.000")
            i.putExtra("simplerXMonthPlan", "Simpler 1 months plan")
            i.putExtra("priceTimesMonths", "IDR 10.000 x 1 months")
            i.putExtra("forXMonths", "(For 1 months)")
            i.putExtra("totalPrice", "IDR 10.000")
            startActivity(i)
        }

        binding.card3months.setOnClickListener {
            i.putExtra("xMonths", " 3 month")
            i.putExtra("months", "3")
            i.putExtra("price", "IDR 8.000")
            i.putExtra("simplerXMonthPlan", "Simpler 3 months plan")
            i.putExtra("priceTimesMonths", "IDR 8.000 x 3 months")
            i.putExtra("forXMonths", "(For 3 months)")
            i.putExtra("totalPrice", "IDR 24.000")
            startActivity(i)
        }

        binding.card6months.setOnClickListener {
            i.putExtra("xMonths", " 6 month")
            i.putExtra("months", "6")
            i.putExtra("price", "IDR 5.000")
            i.putExtra("simplerXMonthPlan", "Simpler 6 months plan")
            i.putExtra("priceTimesMonths", "IDR 5.000 x 6 months")
            i.putExtra("forXMonths", "(For 6 months)")
            i.putExtra("totalPrice", "IDR 30.000")
            startActivity(i)
        }
    }

}