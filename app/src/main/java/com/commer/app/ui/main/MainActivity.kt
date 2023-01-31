package com.commer.app.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.commer.app.R
import androidx.appcompat.app.AppCompatActivity
import com.commer.app.databinding.ActivityMainBinding
import com.commer.app.ui.homepage.HomeViewModel
import com.commer.app.ui.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModelForHome: HomeViewModel by viewModels()
    private val viewModelForProfile: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_activity_example)
        binding.navView.setupWithNavController(navController)

    }

    override fun onBackPressed() {}

    fun getPostFromFilter(tags: String?) {
        lifecycleScope.launch {
            viewModelForHome.getPostFromServer(tags)
        }
    }

    fun deletePostAtProfile(idPost: Int, position: Int) {
        lifecycleScope.launch {
            viewModelForProfile.postDeletePostToServer(idPost, position)
        }
    }

    fun deletePostAtHome(idPost: Int, position: Int) {
        lifecycleScope.launch {
            viewModelForHome.postDeletePostToServer(idPost, position)
        }
    }
}