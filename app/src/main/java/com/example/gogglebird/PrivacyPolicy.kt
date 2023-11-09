package com.example.gogglebird

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient

class PrivacyPolicy : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)


        //variable
        val webView = findViewById<WebView>(R.id.webView)
        webView.webViewClient = WebViewClient()
        //Specify the url
        val url = "assets/privacyPolicy.html"

        webView.loadUrl("file:///android_asset/privacyPolicy.html")
    }
}