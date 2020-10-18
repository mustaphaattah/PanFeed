package com.mtah.panfeed

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.mtah.panfeed.fragments.cases.CasesFragment
import com.mtah.panfeed.fragments.news.NewsFragment
import com.mtah.panfeed.fragments.save.SavedFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val fragments: ArrayList<Fragment> = arrayListOf(NewsFragment(), CasesFragment(), SavedFragment())
    private var fragmentNumber: Int = 0
    private var currentFragment = fragments[0]
    private val savedKey = "index"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolBar)
        setSupportActionBar(toolbar)

        val connectionSnackbar = Snackbar.make( findViewById(R.id.mainActivityLayout),
            "Oops No internet, check your connection.",
            Snackbar.LENGTH_INDEFINITE)
            .setAction("RETRY") {
                recreate()
            }
        //Mobile Ads
        if (!isConnectedToInternet(this.applicationContext)){
            connectionSnackbar.show()
        }
        else {
            MobileAds.initialize(this) {}
            adView.loadAd(AdRequest.Builder().build())


            //set default view to news fragment
            if (savedInstanceState != null) {
                fragmentNumber = savedInstanceState.getInt(savedKey)
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
    }

    //bottom navigation bar Listener
    private val navigationListener =  BottomNavigationView.OnNavigationItemSelectedListener{ item ->
        var selectedFragment: Fragment = NewsFragment()
        when (item.itemId) {
            R.id.nav_news -> selectedFragment = fragments[0]
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

    private fun isConnectedToInternet(context: Context): Boolean {
        val connectivityManager: ConnectivityManager = context
            .getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager

        val networkInfo = connectivityManager.activeNetworkInfo

        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        fragmentNumber = savedInstanceState.getInt(savedKey)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(savedKey, fragments.indexOf(currentFragment))
    }

    companion object {
        const val EXTRA_TITLE = "com.mtah.panfeed.TITLE"
        const val EXTRA_URL = "com.mtah.panfeed.URL"
        const val EXTRA_IMAGE_URL = "com.mtah.panfeed.IMAGEURL"
        const val EXTRA_DATE = "com.mtah.panfeed.DATE"
        private const val TAG = "MainActivity"
    }

}
