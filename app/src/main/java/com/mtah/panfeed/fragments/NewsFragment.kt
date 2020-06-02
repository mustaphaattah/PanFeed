package com.mtah.panfeed.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

import com.mtah.panfeed.R
import com.mtah.panfeed.adapters.PagerAdapter


/**
 * A simple [Fragment] subclass.
 * Use the [NewsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         val view = inflater.inflate(R.layout.fragment_news, container, false)


        val tabLayout: TabLayout = view.findViewById(R.id.tabLayout)
        val viewPager: ViewPager = view.findViewById(R.id.viewPager)

        tabLayout.addTab(tabLayout.newTab().setText("Global"))
        tabLayout.addTab(tabLayout.newTab().setText("Local"))

        val adapter =
            PagerAdapter(this.context!!, childFragmentManager, tabLayout.tabCount)
        viewPager.adapter = adapter

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.currentItem = tab!!.position
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }
        })

        return view
    }
}
