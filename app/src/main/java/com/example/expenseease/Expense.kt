package com.example.expenseease

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import java.util.Calendar

class Expense : AppCompatActivity() {

    private lateinit var btnDatePicker: Button
    private var selectedYear = 0
    private var selectedMonth = 0
    private var selectedDay = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense)

        val home1: LinearLayout = findViewById(R.id.linear_home1)

        home1.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
            // Add the transition animation
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }

        val btnDatePicker: LinearLayout = findViewById(R.id.date)
        btnDatePicker.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        // Get current date as default values for DatePickerDialog
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        // Create a DatePickerDialog and show it
        val datePickerDialog = DatePickerDialog(
            this,
            { view: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                // Handle date selection
                selectedYear = year
                selectedMonth = month
                selectedDay = dayOfMonth
                displaySelectedDate()
            },
            currentYear,
            currentMonth,
            currentDay
        )

        // Optional: Set minimum date (e.g., past dates are disabled)

        datePickerDialog.show()
    }

    private fun displaySelectedDate() {
        val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear" // Note: month is 0-based
        val datetxt: TextView = findViewById(R.id.txtDate)
        datetxt.text = selectedDate
    }
}
