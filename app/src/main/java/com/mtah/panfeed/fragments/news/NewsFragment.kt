package com.mtah.panfeed.fragments.news

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

import com.mtah.panfeed.R
import com.mtah.panfeed.adapters.PagerAdapter
import com.mtah.panfeed.fragments.news.GlobalFragment
import com.mtah.panfeed.fragments.news.LocalFragment

class NewsFragment : Fragment() {

    private val KEY = "position"
    private val TAG = "NewsFragment"
    lateinit var fragments: ArrayList<Fragment>
    var currentFragment = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_news, container, false)

        fragments = arrayListOf(GlobalFragment(), LocalFragment())


        val tabLayout: TabLayout = view.findViewById(R.id.tabLayout)
        val viewPager: ViewPager = view.findViewById(R.id.viewPager)

        val pagerAdapter = PagerAdapter(childFragmentManager)
        pagerAdapter.addFragment("Global", GlobalFragment())
        pagerAdapter.addFragment("Local", LocalFragment())

        viewPager.adapter = pagerAdapter
        if (savedInstanceState != null) {
            currentFragment = savedInstanceState.getInt(KEY)
            viewPager.currentItem = currentFragment
        }

        tabLayout.setupWithViewPager(viewPager)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab?.position
                if (position != null) {
                    viewPager.currentItem = position
                    currentFragment = viewPager.currentItem
                    Log.i(TAG, "onTabSelected: current Tab === $position")
                }

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                val position = tab?.position
                if (position != null) {
                    viewPager.currentItem = position
                    currentFragment = viewPager.currentItem
                    Log.i(TAG, "onTabSelected: current Tab === $position")
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY, currentFragment)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            currentFragment = savedInstanceState.getInt(KEY)
        }
    }
}
