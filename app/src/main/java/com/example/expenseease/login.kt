package com.example.expenseease

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class Login : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        val signup: TextView = findViewById(R.id.signup)
        val btn_login: AppCompatButton = findViewById(R.id.btn_login)
        val textview3: TextView = findViewById(R.id.textView3)


        textview3.visibility = View.VISIBLE
        signup.visibility = View.VISIBLE

        signup.setOnClickListener {
            val intent = Intent(this, Sign_up::class.java)
            startActivity(intent)
        }

        btn_login.setOnClickListener {
            val username = findViewById<EditText>(R.id.username).text.toString()
            val password = findViewById<EditText>(R.id.editText).text.toString()

            if (authenticateUser(username, password)) {
                startMainActivity(username)
            } else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun authenticateUser(username: String, password: String): Boolean {

        val storedPassword = sharedPreferences.getString(username, null)

        return storedPassword != null && storedPassword == password
    }

    private fun startMainActivity(username: String) {

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("username", username)
        startActivity(intent)
        // Finish the current activity to prevent the user from coming back to the login screen after logging in
        finish()
    }
}

