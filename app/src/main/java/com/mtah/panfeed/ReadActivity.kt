package com.mtah.panfeed

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.mtah.panfeed.api.GlideApp
import com.mtah.panfeed.fragments.save.SavedViewModel
import com.mtah.panfeed.models.Article

class ReadActivity : AppCompatActivity() {

    private lateinit var newsWebView: WebView
    private lateinit var newsImageView: ImageView
    private lateinit var newsProgressBar: ProgressBar
    private lateinit var newsUrl: String
    private lateinit var newsIntent: Intent
    private lateinit var savedViewModel: SavedViewModel
    private var displayArticle: Article? = null

    private val TAG = "ReadActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read)

        val toolbar = findViewById<Toolbar>(R.id.newsToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        savedViewModel = ViewModelProvider(this).get(SavedViewModel::class.java)

        newsWebView = findViewById(R.id.newsWebView)
        newsImageView = findViewById(R.id.newsImageView)
        newsProgressBar = findViewById(R.id.newsProgressBar)
        newsProgressBar.visibility = View.VISIBLE

        newsIntent = intent
        if ( newsIntent.hasExtra(MainActivity.EXTRA_TITLE)
            && newsIntent.hasExtra(MainActivity.EXTRA_URL)
            && newsIntent.hasExtra(MainActivity.EXTRA_IMAGE_URL) ){

            if (newsIntent.hasExtra(MainActivity.EXTRA_DATE)) {
                displayArticle = Article(
                    newsIntent.getStringExtra(MainActivity.EXTRA_TITLE)!!,
                    newsIntent.getStringExtra(MainActivity.EXTRA_URL)!!,
                    newsIntent.getStringExtra(MainActivity.EXTRA_IMAGE_URL)!!,
                    newsIntent.getStringExtra(MainActivity.EXTRA_DATE)!!
                )
            }

        } else {
            Toast.makeText(this, "Cannot get Article", Toast.LENGTH_SHORT).show()
        }

        initReader()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.read_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share_option -> {
                shareArticle()
            }
            R.id.save_option -> {
                saveArticle()
            }
            R.id.open_browser_option -> {
                openInBrowser()
            }
            android.R.id.home -> this.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun initReader() {

        if (newsIntent.hasExtra(MainActivity.EXTRA_TITLE)){
            val title = newsIntent.getStringExtra(MainActivity.EXTRA_TITLE)
            supportActionBar?.title = title
        }

        if (newsIntent.hasExtra(MainActivity.EXTRA_IMAGE_URL)){
            val imageUrl = newsIntent.getStringExtra(MainActivity.EXTRA_IMAGE_URL)
            GlideApp.with(this)
                .load(imageUrl)
                .centerCrop()
                .fallback(R.drawable.no_img)
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.no_img)
                .into(newsImageView)
        }

        if (newsIntent.hasExtra(MainActivity.EXTRA_URL)) {
//            newsWebView.settings.loadsImagesAutomatically = false
            newsWebView.webViewClient = WebViewClient()
            newsUrl = newsIntent.getStringExtra(MainActivity.EXTRA_URL)!!
            newsWebView.loadUrl(newsUrl)

        } else {
            Toast.makeText(this, "No news link", Toast.LENGTH_SHORT)
        }

        Log.i(TAG, "initReader: initialized")
    }

    private fun shareArticle() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Article shared through Panfeed")
        shareIntent.putExtra(Intent.EXTRA_TEXT, newsUrl)
        startActivity(Intent.createChooser(shareIntent, "Share via:"))
    }

    private fun saveArticle(){
        if (displayArticle != null) {
            savedViewModel.insert(displayArticle!!)
            Toast.makeText(this, "Article saved", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Cannot save this", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openInBrowser() {
        if (newsIntent.hasExtra(MainActivity.EXTRA_URL)) {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(newsIntent.getStringExtra(MainActivity.EXTRA_URL)))
            startActivity(browserIntent)
        } else {
            Toast.makeText(this, "Cannot open in browser.", Toast.LENGTH_SHORT).show()
        }
    }
}
