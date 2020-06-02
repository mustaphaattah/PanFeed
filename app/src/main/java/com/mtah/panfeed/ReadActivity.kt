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
import kotlinx.android.synthetic.main.activity_read.*

class ReadActivity : AppCompatActivity() {

    lateinit var newsWebView: WebView
    lateinit var newsImageView: ImageView
    lateinit var newsProgressBar: ProgressBar
    lateinit var newsUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read)

        var toolbar = findViewById<Toolbar>(R.id.newsToolbar)
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
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
            }
            R.id.save_option -> {
                Toast.makeText(this, "Save an article", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun initReader(){
        var newsIntent = intent

        if (intent.hasExtra("image")){
            var imageUrl = newsIntent.getStringExtra("image")
            GlideApp.with(this)
                .load(imageUrl)
                .fallback(R.drawable.no_img)
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.no_img)
                .centerCrop()
                .into(newsImageView)
        }

        if (intent.hasExtra("title")){
            var title = newsIntent.getStringExtra("title")
            supportActionBar?.title = title
        }

        if (newsIntent.hasExtra("url")) {
            newsWebView.settings.loadsImagesAutomatically = true
            newsWebView.settings.javaScriptEnabled = true
            newsWebView.webViewClient = WebViewClient()
            newsUrl = newsIntent.getStringExtra("url")
            newsWebView.loadUrl(newsUrl)

        } else {
            Toast.makeText(this, "No news link", Toast.LENGTH_SHORT)
        }
    }

    private fun shareArticle() {
        var shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Article shared through Panfeed")
        shareIntent.putExtra(Intent.EXTRA_TEXT, newsUrl)
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }
}
