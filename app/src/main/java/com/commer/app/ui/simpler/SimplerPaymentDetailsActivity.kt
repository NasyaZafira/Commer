package com.commer.app.ui.simpler

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.commer.app.databinding.ActivitySimplerPaymentDetailsBinding
import java.text.SimpleDateFormat
import java.util.*

class SimplerPaymentDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySimplerPaymentDetailsBinding
    private var plan = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySimplerPaymentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgArrowBack.setOnClickListener { onBackPressed() }

        intent.extras?.let {
            val xMonths = it.getString("xMonths")
            val price = it.getString("price")
            val simplerXMonthPlan = it.getString("simplerXMonthPlan")
            val priceTimesMonths = it.getString("priceTimesMonths")
            val forXMonths = it.getString("forXMonths")
            val totalPrice = it.getString("totalPrice")
            plan = it.getString("months").toString()

            binding.txtXMonths1.text = xMonths
            binding.txtSimplerPrice.text = price
            binding.txtXMonths2.text = xMonths
            binding.txtSimplerXMonthsPlan.text = simplerXMonthPlan
            binding.txtPrice.text = priceTimesMonths
            binding.txtForXMonths.text = forXMonths
            binding.txtTotalPrice.text = totalPrice
        }

        val plan = intent.extras?.getString("months").toString()

        binding.btnChangePlan.setOnClickListener { onBackPressed() }

        binding.btnNextPayment.setOnClickListener {
            val i = Intent(this, SimplerPaymentUploadActivity::class.java)

            val formatDateTime = SimpleDateFormat("yyMMddHHmmss", Locale.getDefault())
            val currentDateTime = formatDateTime.format(Date())
            val formatTransactionDate =
                SimpleDateFormat("MMMM dd yyyy,\nhh:mm a", Locale.getDefault())
            val currentTransactionDate = formatTransactionDate.format(Date())

            i.putExtra("currentDateTime", currentDateTime)
            i.putExtra("plan", plan)
            i.putExtra("currentTransactionDate", currentTransactionDate)

            startActivity(i)
        }

    }

}