package com.commer.app.ui.simplerpost

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SimplerPostAdapter(fragment: SimplerPostsFragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OAFragment()
            else -> VAFragment()
        }
    }
}
