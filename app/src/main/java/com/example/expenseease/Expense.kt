package com.example.expenseease

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import java.util.Calendar

class Expense : AppCompatActivity() {
    private lateinit var db: AppDatabase

    private var selectedYear = 0
    private var selectedMonth = 0
    private var selectedDay = 0

    private lateinit var amountEditText: EditText
    private lateinit var categoryEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var dateTextView: TextView
    private lateinit var expensesAdapter: ExpensesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense)

        db = AppDatabase.getDatabase(applicationContext)

        amountEditText = findViewById(R.id.et_amt)
        categoryEditText = findViewById(R.id.et_cat)
        dateTextView = findViewById(R.id.txtDate)
        saveButton = findViewById(R.id.btn_save)
        setupRecyclerView()

        saveButton.setOnClickListener {
            saveExpense()
        }

        val homeButton: LinearLayout = findViewById(R.id.linear_home1)
        homeButton.setOnClickListener {
            finish()
        }

       val btnDatePicker: LinearLayout = findViewById(R.id.date)
        btnDatePicker.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.rv_exp)
        expensesAdapter = ExpensesAdapter(emptyList())
        recyclerView.adapter = expensesAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        db.expenseDao().getAllExpenses().observe(this, Observer { expenses ->
            expensesAdapter.updateExpenses(expenses)
        })
    }

    private fun saveExpense() {
        val amount = amountEditText.text.toString().toDoubleOrNull() ?: 0.0
        val category = categoryEditText.text.toString()
        val date = dateTextView.text.toString()

        val expense = ExpenseItem(amount = amount, category = category, date = date)
        lifecycleScope.launch {
            db.expenseDao().insert(expense)
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            selectedYear = year
            selectedMonth = month
            selectedDay = dayOfMonth
            displaySelectedDate()
        }, currentYear, currentMonth, currentDay)

        datePickerDialog.show()
    }

    private fun displaySelectedDate() {
        val selectedDate = "${selectedDay + 1}/${selectedMonth + 1}/$selectedYear"
        dateTextView.text = selectedDate
    }
}
