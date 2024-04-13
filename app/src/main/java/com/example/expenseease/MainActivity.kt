package com.example.expenseease

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val img_exp: ImageView = findViewById(R.id.img_exp)

        img_exp.setOnClickListener {
            val i = Intent(this, Expense::class.java)
            startActivity(i)
            // Add the transition animation
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }
}
