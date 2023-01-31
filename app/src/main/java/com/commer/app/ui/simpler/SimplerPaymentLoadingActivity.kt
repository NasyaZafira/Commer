package com.commer.app.ui.simpler

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.commer.app.databinding.ActivitySimplerPaymentLoadingBinding
import com.commer.app.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@AndroidEntryPoint
class SimplerPaymentLoadingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySimplerPaymentLoadingBinding
    private val viewModel: SimplerPaymentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySimplerPaymentLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnReturnToHomepage.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }

    }

    override fun onStart() {
        super.onStart()
        binding.swipeToRefresh.setOnRefreshListener {
            lifecycleScope.launch {
                viewModel.getPaymentStatusFromServer()
            }
            binding.swipeToRefresh.isRefreshing = false
            setupObserver()
        }
    }

    override fun onBackPressed() {
        val fromPayment = intent.getStringExtra("fromPayment")
        if (fromPayment == null) {
            super.onBackPressed()
        }
    }

    private fun setupObserver() {
        viewModel.getPaymentStatusResponse.observe(this) {
            when (it.message) {
                "On Progress" -> {
                }
                "Failed" -> {
                    val i = Intent(this, SimplerPaymentFailedActivity::class.java)
                    startActivity(i)
                }
                "success" -> {
                    val i = Intent(this, SimplerPaymentSuccessActivity::class.java)
                    val responseDate = it.data[0].createdDate
                    val responseDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                    val parseDate = responseDateFormat.parse(responseDate)
                    val newDateFormat = SimpleDateFormat("MMMM dd yyyy,\nhh:mm aaa")
                    val finalDate = newDateFormat.format(parseDate!!)
                    i.putExtra("date", finalDate)
                    i.putExtra("price", it.data[0].totalPaid)
                    i.putExtra("plan details", "Simpler " + it.data[0].plan + " months plan")
                    i.putExtra("for x month", "(For " + it.data[0].plan + " months)")
                    startActivity(i)
                }
                else -> {
                    Toast.makeText(this, "Error message: ${it.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
//        val i = Intent(this, SimplerPaymentFailedActivity::class.java)
//        startActivity(i)
}

