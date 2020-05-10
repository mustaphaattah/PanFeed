package com.mtah.panfeed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.widget.Toast

class ReadActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read)
        window.statusBarColor = getColor(R.color.colorPrimaryDark)

        var newsIntent = intent
        if (newsIntent.hasExtra("url")) {
            var webView: WebView = findViewById(R.id.newsWebView)
            webView.loadUrl(newsIntent.getStringExtra("url"))
        } else {
            Toast.makeText(this, "No news link", Toast.LENGTH_SHORT)
        }


    }
}
