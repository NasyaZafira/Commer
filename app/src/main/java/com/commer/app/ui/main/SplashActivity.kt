package com.commer.app.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.commer.app.data.local.CommerSharedPref
import com.commer.app.databinding.ActivitySplashBinding
import com.commer.app.ui.welcomepage.WelcomeActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private var _binding: ActivitySplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val splashTime: Long = 3000

        Handler(Looper.getMainLooper()).postDelayed({
            val checkLogin = CommerSharedPref.isLoggedIn
            val intent = if (!checkLogin) Intent(this, WelcomeActivity::class.java)
            else Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, splashTime)
    }

    override fun onPause() {
        super.onPause()
        _binding = null
    }
}