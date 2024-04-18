package com.example.expenseease

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class Sign_up : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()

        val login: TextView = findViewById(R.id.tv_login)
        val btn_signup: TextView = findViewById(R.id.btn_signup)
        val edtusername: EditText =
            findViewById(R.id.editText)  // Assuming this is the ID for the username input

        login.setOnClickListener {
            val i = Intent(this, Login::class.java)
            startActivity(i)
            finish()
        }

        btn_signup.setOnClickListener {
            val username = edtusername.text.toString().trim()
            val email = findViewById<EditText>(R.id.editText3).text.toString().trim()
            val password = findViewById<EditText>(R.id.editText2).text.toString().trim()

            if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                // Attempt to create a new user with email and password
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // User creation is successful
                            val user = auth.currentUser
                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(username) // Setting the username as the display name
                                .build()

                            // Attempt to update the user's profile with the new display name
                            user?.updateProfile(profileUpdates)
                                ?.addOnCompleteListener { profileTask ->
                                    if (profileTask.isSuccessful) {
                                        // Profile update successful, proceed to Main Activity
                                        startMainActivity(username)
                                    } else {
                                        // Profile update failed, show an error message
                                        Toast.makeText(
                                            baseContext,
                                            "Failed to update user profile: ${profileTask.exception?.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        } else {
                            // User creation failed, show an error message
                            Toast.makeText(
                                baseContext, "Authentication failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(
                    this,
                    "Please enter username, email, and password",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

        private fun startMainActivity(username: String) {
        val intent = Intent(this, Intro_page::class.java)
        intent.putExtra("username", username)
        startActivity(intent)
        finish()  // Finish the current activity to prevent the user from coming back to the sign-up screen after logging in
    }
}
