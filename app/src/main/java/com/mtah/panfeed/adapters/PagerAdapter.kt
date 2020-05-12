package com.mtah.panfeed.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.mtah.panfeed.fragments.GlobalFragment
import com.mtah.panfeed.fragments.LocalFragment

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