package com.example.expenseease

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExpenseDao {
    @Insert
    fun insert(expense: ExpenseItem)

    @Query("SELECT * FROM expenses ORDER BY id DESC")
    fun getAllExpenses(): LiveData<List<ExpenseItem>>
}

