package com.mtah.panfeed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mtah.panfeed.fragments.CasesFragment
import com.mtah.panfeed.fragments.NewsFragment
import com.mtah.panfeed.fragments.SavedFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolBar)
        setSupportActionBar(toolbar)

        //set default view to news fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, NewsFragment())
            .commit()

        //bottom navigation bar
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener(navigationListener)

    }

    //bottom navigation bar Listener
    private var navigationListener =  BottomNavigationView.OnNavigationItemSelectedListener{ item ->
        var selectedFragment: Fragment = NewsFragment()
        when (item.itemId) {
            R.id.nav_news -> selectedFragment = NewsFragment()
            R.id.nav_cases -> selectedFragment = CasesFragment()
            R.id.nav_saved -> selectedFragment = SavedFragment()
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, selectedFragment)
            .commit()

        true
    }

    companion object {
        const val EXTRA_TITLE = "com.mtah.panfeed.TITLE"
        const val EXTRA_URL = "com.mtah.panfeed.URL"
        const val EXTRA_IMAGE_URL = "com.mtah.panfeed.IMAGEURL"
        const val EXTRA_DATE = "com.mtah.panfeed.DATE"
    }

}
