package com.commer.app.ui.resetpassword.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.commer.app.data.model.remote.user.detail.DetailProfile
import com.commer.app.databinding.ActivityResetPasswordSuccessBinding
import com.commer.app.ui.login.LoginActivity
import com.commer.app.ui.settings.SettingsActivity

class ResetPasswordSuccessActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetPasswordSuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResetPasswordSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getSettings = intent.getStringExtra("fromSettings")
        val getData = intent.getParcelableExtra<DetailProfile>("getData")
        if (getSettings != null) {
            binding.btnReturnSettings.visibility = View.VISIBLE
            binding.btnReturnLogin.visibility = View.INVISIBLE
        }
        binding.btnReturnSettings.setOnClickListener {
            val i = Intent(this, SettingsActivity::class.java)
            i.putExtra("getData", getData)
            startActivity(i)
            finish()
        }
        binding.btnReturnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}