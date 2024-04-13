package com.example.expenseease

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val exp: LinearLayout = findViewById(R.id.linear_exp)

        exp.setOnClickListener {
            val i = Intent(this, Expense::class.java)
            startActivity(i)
            // Add the transition animation
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }

        val circularImageView: ImageView = findViewById(R.id.circularImageView)

        // Set click listener for circularImageView
        circularImageView.setOnClickListener {
            // Get the DrawerLayout from the parent activity
            val drawerLayout: DrawerLayout? = findViewById(R.id.drawer_layout)
            // Open the drawer if the DrawerLayout is not null
            drawerLayout?.openDrawer(GravityCompat.START)
        }
    }
}
