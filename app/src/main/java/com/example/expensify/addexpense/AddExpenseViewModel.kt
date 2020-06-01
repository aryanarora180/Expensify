package com.example.expensify.addexpense

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expensify.R
import com.example.expensify.helper.Expense
import com.example.expensify.helper.FirestoreRepository
import java.text.SimpleDateFormat
import java.util.*

class AddExpenseViewModel(private val application: Application) : ViewModel() {

    private val TAG = "DashboardViewModel"
    private val repository = FirestoreRepository(application)

    val formatter =
        SimpleDateFormat(application.getString(R.string.date_format), Locale.getDefault())

    fun saveExpense(expense: Expense) {
        repository.saveExpense(expense).addOnFailureListener {
            Log.e(TAG, "Failed to save Address!")
        }
    }

    fun formatDate(date: Date): String {
        return "${application.getString(R.string.date)} ${formatter.format(date)}"
    }

}

class AddExpenseViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddExpenseViewModel(
            application
        ) as T
    }
}