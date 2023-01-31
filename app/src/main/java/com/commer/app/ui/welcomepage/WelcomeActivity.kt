package com.commer.app.ui.welcomepage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.commer.app.databinding.ActivityWelcomePageBinding
import com.commer.app.ui.login.LoginActivity
import com.commer.app.ui.register.SignUpFormActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomePageBinding
    private lateinit var viewPagerWelcome: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPagerWelcome = binding.viewPager
        val viewPagerAdapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = viewPagerAdapter

        binding.wormDotsIndicator.setViewPager2(viewPagerWelcome)
    }

    fun moveToSecondScreen() {
        viewPagerWelcome.setCurrentItem(1, true)
    }

    fun moveToThirdScreen() {
        viewPagerWelcome.setCurrentItem(2, true)
    }

    fun moveToSignUp() {
        val intent = Intent(this, SignUpFormActivity::class.java)
        startActivity(intent)
    }

    fun moveToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

}

