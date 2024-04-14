package com.example.expenseease

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
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

        var letsgo: AppCompatButton = findViewById(R.id.btn_letsgo)

        letsgo.setOnClickListener(){
            val income: EditText = findViewById(R.id.income) // Correctly reference the EditText
            val amountEntered = income.text.toString()
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("INCOME_AMOUNT", amountEntered)
            startActivity(intent)
            finish()
        }

    }
}