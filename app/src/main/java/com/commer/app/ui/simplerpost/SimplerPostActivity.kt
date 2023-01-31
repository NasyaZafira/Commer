package com.commer.app.ui.simplerpost

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.commer.app.databinding.ActivitySimplerPostBinding

class SimplerPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySimplerPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySimplerPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.viewPager.apply {
//            this.adapter = ViewPagerAdapter(this@SimplerPostActivity)
//        }
//
//        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
//            tab.text = when (position) {
//                0 -> "Official Account"
//                else -> "Verified Account"
//            }
//        }.attach()
    }
}