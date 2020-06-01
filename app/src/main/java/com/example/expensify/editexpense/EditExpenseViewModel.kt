package com.example.expensify.editexpense

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expensify.helper.Expense
import com.example.expensify.helper.FirestoreExpense
import com.example.expensify.helper.FirestoreRepository
import java.text.SimpleDateFormat
import java.util.*

class EditExpenseViewModel(private val application: Application) : ViewModel() {

    private val TAG = "DashboardViewModel"
    private val repository = FirestoreRepository(application)

    private val formatter = SimpleDateFormat("MMMM dd", Locale.getDefault())

    fun editExpense(previousExpense: FirestoreExpense, newExpense: Expense) {
        repository.editExpense(previousExpense, convertExpenseToMap(newExpense))
    }

    fun formatDate(dateToFormat: Date): String {
        return "Date: ${formatter.format(dateToFormat)}"
    }

    fun convertExpenseToMap(expense: Expense): Map<String, Any?> {
        return mapOf<String, Any?>(
            "amount" to expense.amount,
            "merchant" to expense.merchant,
            "description" to expense.description,
            "geoPoint" to expense.geoPoint,
            "date" to expense.date
        )
    }

}

class EditExpenseViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EditExpenseViewModel(
            application
        ) as T
    }
}