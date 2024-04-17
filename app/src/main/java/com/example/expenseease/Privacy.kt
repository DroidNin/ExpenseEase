package com.example.expenseease

import android.os.Bundle
import android.webkit.WebView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Privacy : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy)


        val web:WebView = findViewById(R.id.webView)
        web.loadUrl("https://www.termsfeed.com/live/d1cca3e9-370c-4d16-b9a2-9622fb352747")
    }
}