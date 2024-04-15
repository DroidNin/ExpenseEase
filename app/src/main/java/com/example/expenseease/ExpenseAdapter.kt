package com.example.expenseease

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExpensesAdapter(private var expensesList: List<ExpenseItem>) : RecyclerView.Adapter<ExpensesAdapter.ExpenseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.expense_list, parent, false)
        return ExpenseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val currentItem = expensesList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount() = expensesList.size

    // Update the list of expenses and notify the RecyclerView of data change
    fun updateExpenses(expenses: List<ExpenseItem>) {
        this.expensesList = expenses
        notifyDataSetChanged()
    }

    class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val amountTextView: TextView = itemView.findViewById(R.id.amt_list)
        private val categoryTextView: TextView = itemView.findViewById(R.id.cat_list)
        private val dateTextView: TextView = itemView.findViewById(R.id.date_list)

        fun bind(expense: ExpenseItem) {
            amountTextView.text = expense.amount.toString()
            categoryTextView.text = expense.category
            dateTextView.text = expense.date
        }
    }
}
