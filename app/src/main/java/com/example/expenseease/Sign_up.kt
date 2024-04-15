package com.example.expenseease

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class Sign_up : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        val login: TextView = findViewById(R.id.tv_login)
        val btn_signup: TextView = findViewById(R.id.btn_signup)

        login.setOnClickListener() {
            val i = Intent(this, Login::class.java)
            startActivity(i)
        }

        btn_signup.setOnClickListener() {
            val username = findViewById<EditText>(R.id.editText).text.toString()
            val password = findViewById<EditText>(R.id.editText2).text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                // Check if username already exists
                if (!sharedPreferences.contains(username)) {
                    // Store user data
                    sharedPreferences.edit().putString(username, password).apply()
                    // Redirect to main activity
                    startMainActivity(username)
                } else {
                    // Username already exists, show error message
                    Toast.makeText(
                        this,
                        "Username already exists. Please choose a different username.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                // Username or password field is empty, show error message
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    private  fun startMainActivity(username: String) {
        val intent = Intent(this, Intro_page::class.java)
        intent.putExtra("username", username)
        startActivity(intent)
        finish() // Finish the current activity to prevent the user from coming back to the sign-up screen after logging in
    }
}
