package com.example.expenseease

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

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
            val email = findViewById<EditText>(R.id.email).text.toString().trim()
            val password = findViewById<EditText>(R.id.editText).text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = auth.currentUser
                            startMainActivity(user?.email ?: "")
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this, "Authentication failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startMainActivity(email: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("email", email)
        startActivity(intent)
        finish() // Finish the current activity to prevent the user from coming back to the login screen after logging in
    }
}


