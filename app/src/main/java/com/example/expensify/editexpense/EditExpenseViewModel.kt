package com.example.expensify.editexpense

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expensify.R
import com.example.expensify.helper.Expense
import com.example.expensify.helper.FirestoreExpense
import com.example.expensify.helper.FirestoreRepository
import java.text.SimpleDateFormat
import java.util.*

class EditExpenseViewModel(private val application: Application) : ViewModel() {

    private val TAG = "DashboardViewModel"
    private val repository = FirestoreRepository(application)

    private val formatter =
        SimpleDateFormat(application.getString(R.string.date_format), Locale.getDefault())

    fun editExpense(previousExpense: FirestoreExpense, newExpense: Expense) {
        repository.editExpense(previousExpense, convertExpenseToMap(newExpense))
    }

    fun formatDate(dateToFormat: Date): String {
        return "Date: ${formatter.format(dateToFormat)}"
    }

    fun convertExpenseToMap(expense: Expense): Map<String, Any?> {
        return mapOf<String, Any?>(
            application.getString(R.string.firestore_expense_field_amount) to expense.amount,
            application.getString(R.string.firestore_expense_field_merchant) to expense.merchant,
            application.getString(R.string.firestore_expense_field_description) to expense.description,
            application.getString(R.string.firestore_expense_field_location) to expense.geoPoint,
            application.getString(R.string.firestore_expense_field_date) to expense.date
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