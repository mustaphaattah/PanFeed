package com.mtah.panfeed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.mtah.panfeed.api.GlideApp

class ReadActivity : AppCompatActivity() {

    private lateinit var newsWebView: WebView
    private lateinit var newsImageView: ImageView
    private lateinit var newsProgressBar: ProgressBar
    private lateinit var newsUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read)

        val toolbar = findViewById<Toolbar>(R.id.newsToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        newsWebView = findViewById(R.id.newsWebView)
        newsImageView = findViewById(R.id.newsImageView)
        newsProgressBar = findViewById(R.id.newsProgressBar)
        newsProgressBar.visibility = View.VISIBLE

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
//                TODO("saveArticle()")
                Toast.makeText(this, "Article Saved", Toast.LENGTH_SHORT).show()
            }
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

//    override fun onBackPressed() {
//        val backIntent = Intent(this, MainActivity::class.java)
//        backIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
//        startActivity(backIntent)
//        finish()
//    }
    private fun saveArticle(){
        val newsIntent = intent
        if (newsIntent.hasExtra("image") && newsIntent.hasExtra("title") &&
            newsIntent.hasExtra("url") && newsIntent.hasExtra("date")) {
//            TODO("save article")
            return
        }
        Toast.makeText(this, "Cannot save this article", Toast.LENGTH_SHORT)
    }

    private fun initReader(){
        val newsIntent = intent

        if (newsIntent.hasExtra("image")){
            val imageUrl = newsIntent.getStringExtra("image")
            GlideApp.with(this)
                .load(imageUrl)
                .fallback(R.drawable.no_img)
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.no_img)
                .centerCrop()
                .into(newsImageView)
        }

        if (newsIntent.hasExtra("title")){
            val title = newsIntent.getStringExtra("title")
            supportActionBar?.title = title
        }

        if (newsIntent.hasExtra("url")) {
            newsWebView.settings.loadsImagesAutomatically = false
            newsWebView.settings.javaScriptEnabled = true
            newsWebView.settings.domStorageEnabled = true
            newsWebView.webViewClient = WebViewClient()
            newsUrl = newsIntent.getStringExtra("url")!!
            newsWebView.loadUrl(newsUrl)
        } else {
            Toast.makeText(this, "No news link", Toast.LENGTH_SHORT)
        }
    }

    private fun shareArticle() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Article shared through Panfeed")
        shareIntent.putExtra(Intent.EXTRA_TEXT, newsUrl)
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }
}
