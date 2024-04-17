package com.example.expenseease

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Set_income : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_income)


//        enableEdgeToEdge()
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        val incomeEditText: EditText = findViewById(R.id.income)
        val btnLetsGo: AppCompatButton = findViewById(R.id.btn_letsgo)

        btnLetsGo.setOnClickListener {
            val incomeStr = incomeEditText.text.toString()
            if (incomeStr.isNotEmpty()) {
                val income = incomeStr.toInt()

                // Store the income using SharedPreferences
                val sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putInt("income", income)
                editor.apply()

                // Start MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please enter your income", Toast.LENGTH_SHORT).show()
            }
        }
    }
}