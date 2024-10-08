package com.example.expenseease

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class Expense : AppCompatActivity() {
    private lateinit var db: AppDatabase

    private var selectedYear = 0
    private var selectedMonth = 0
    private var selectedDay = 0

    private lateinit var amountEditText: EditText
    private lateinit var spinner: Spinner
    private lateinit var saveButton: Button
    private lateinit var dateTextView: TextView
    private lateinit var expensesAdapter: ExpensesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense)

        spinner = findViewById(R.id.et_cat)  // Make sure ID matches your layout XML
        val categories = arrayOf("Food & Drinks", "Clothing", "Medical", "Entertainment", "Travel",
            "Grocery", "Internet", "Electricity", "Water", "Rent", "Insurance",
            "Credit", "Book", "Gift", "Others")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        spinner.adapter = adapter

        db = AppDatabase.getDatabase(applicationContext)

        amountEditText = findViewById(R.id.et_amt)
        dateTextView = findViewById(R.id.txtDate)
        saveButton = findViewById(R.id.btn_save)
        setupRecyclerView()

        saveButton.setOnClickListener {
            if (validateInput()) {
                saveExpense()
            }
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

    private fun validateInput(): Boolean {
        if (amountEditText.text.toString().isEmpty() || amountEditText.text.toString().toDoubleOrNull() == null) {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            return false
        }
        if (spinner.selectedItem == null) {
            Toast.makeText(this, "Category cannot be empty", Toast.LENGTH_SHORT).show()
            return false
        }
        if (dateTextView.text.toString() == "Select date") {
            Toast.makeText(this, "Select date", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun saveExpense() {
        val amount = amountEditText.text.toString().toDouble() // Safe to call after validation
        val category = spinner.selectedItem.toString()
        val date = dateTextView.text.toString()

        val expense = ExpenseItem(amount = amount, category = category, date = date)
        lifecycleScope.launch(Dispatchers.IO) {
            db.expenseDao().insert(expense)
            launch(Dispatchers.Main) {
                Toast.makeText(this@Expense, "Expense saved successfully", Toast.LENGTH_SHORT).show()
                updateAvailableAmount(amount)
            }
        }
    }

    private fun updateAvailableAmount(expenseAmount: Double) {
        val sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)
        val currentAmount = sharedPreferences.getFloat("amountLeft", 0f)
        val newAmount = currentAmount - expenseAmount.toFloat()
        sharedPreferences.edit().putFloat("amountLeft", newAmount).apply()
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
        val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear" // Adjust for zero-based month index
        dateTextView.text = selectedDate
    }
}
