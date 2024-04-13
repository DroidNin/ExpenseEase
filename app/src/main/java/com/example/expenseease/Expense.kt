package com.example.expenseease

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class Expense : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense)

        val img_home1: ImageView = findViewById(R.id.img_home1)

        img_home1.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            // Add the transition animation
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }
}
