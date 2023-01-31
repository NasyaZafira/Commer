package com.commer.app.ui.report

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.commer.app.R
import com.commer.app.databinding.ActivityReportSuccessBinding

class ReportSuccessActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportSuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Commit_Home)
        binding = ActivityReportSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgBtnBack.setOnClickListener {
            onBackPressed()
        }
    }
}