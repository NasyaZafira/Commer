package com.commer.app.ui.homepage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.commer.app.databinding.ActivityVideoBinding

class VideoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoBinding

    companion object {
        const val INTENT_VIDEO_URL = "INTENT_VIDEO_URL"
        const val INTENT_VIDEO_PATH = "INTENT_VIDEO_PATH"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val target =
            intent.getStringExtra(INTENT_VIDEO_URL) ?: intent.getStringExtra(INTENT_VIDEO_PATH)
        binding.exoPlayer.setSource(target!!)
        binding.imgExit.setOnClickListener { finish() }
    }

    override fun onPause() {
        binding.exoPlayer.pausePlayer()
        super.onPause()
    }
}