package com.example.gogglebird

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient

class TopSightings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_sightings)

        //variable
        val webView = findViewById<WebView>(R.id.webView)
        webView.webViewClient = WebViewClient()
        //Specify the url
        val url = "https://ebird.org/region/saf"
        webView.loadUrl(url)
    }
}