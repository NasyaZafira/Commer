package com.commer.app.ui.settings.contact

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.commer.app.R
import com.commer.app.databinding.ActivityContactUsBinding

class ContactUsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Commit_Home)
        binding = ActivityContactUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgBtnBack.setOnClickListener {
            onBackPressed()
        }

    }
}