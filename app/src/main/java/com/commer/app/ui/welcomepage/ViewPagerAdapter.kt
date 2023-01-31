package com.commer.app.ui.welcomepage

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.commer.app.ui.welcomepage.screens.FirstScreen
import com.commer.app.ui.welcomepage.screens.SecondScreen
import com.commer.app.ui.welcomepage.screens.ThirdScreen

class ViewPagerAdapter(activity: WelcomeActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FirstScreen()
            1 -> SecondScreen()
            else -> ThirdScreen()
        }
    }

}