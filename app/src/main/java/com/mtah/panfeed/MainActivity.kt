package com.mtah.panfeed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mtah.panfeed.fragments.CasesFragment
import com.mtah.panfeed.fragments.NewsFragment
import com.mtah.panfeed.fragments.SavedFragment

class MainActivity : AppCompatActivity() {

    val fragments: ArrayList<Fragment> = arrayListOf(NewsFragment(), CasesFragment(), SavedFragment())
    var fragmentNumber: Int = 0
    private var currentFragment = fragments[0]
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolBar)
        setSupportActionBar(toolbar)

        //set default view to news fragment
        if (savedInstanceState != null) {
            Log.i(TAG, "onCreate: savedInstanceState = ${savedInstanceState.getInt("index")}")
            fragmentNumber = savedInstanceState.getInt("index")
            Log.i(TAG, "onCreate: fragment Number = $fragmentNumber")
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragments[fragmentNumber])
                .commit()
            currentFragment = fragments[fragmentNumber]
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, NewsFragment())
                .commit()
            fragmentNumber = fragments.indexOf(currentFragment)
        }

        //bottom navigation bar
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener(navigationListener)

    }

    //bottom navigation bar Listener
    private val navigationListener =  BottomNavigationView.OnNavigationItemSelectedListener{ item ->
        var selectedFragment: Fragment = NewsFragment()
        when (item.itemId) {
            R.id.nav_news -> {
                selectedFragment = fragments[0]
            }
            R.id.nav_cases -> selectedFragment =  fragments[1]
            R.id.nav_saved -> selectedFragment =  fragments[2]
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, selectedFragment)
            .commit()

        currentFragment = selectedFragment
        fragmentNumber = fragments.indexOf(currentFragment)
        true
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.i(TAG, "onRestoreInstanceState: fragmentNumber = $fragmentNumber")
        fragmentNumber = savedInstanceState.getInt("index")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState: current Fragment = $fragmentNumber")
        outState.putInt("index", fragments.indexOf(currentFragment))
    }

    companion object {
        const val EXTRA_TITLE = "com.mtah.panfeed.TITLE"
        const val EXTRA_URL = "com.mtah.panfeed.URL"
        const val EXTRA_IMAGE_URL = "com.mtah.panfeed.IMAGEURL"
        const val EXTRA_DATE = "com.mtah.panfeed.DATE"
    }

}
