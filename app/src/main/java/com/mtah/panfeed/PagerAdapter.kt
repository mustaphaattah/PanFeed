package com.mtah.panfeed

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PagerAdapter(private val context: Context, manager: FragmentManager, var tabCount: Int) : FragmentPagerAdapter(manager) {
    override fun getItem(position: Int): Fragment? {
        return when (position) {
            0 -> GlobalFragment()
            else -> LocalFragment()
        }
    }

    override fun getCount(): Int {
        return tabCount
    }

}