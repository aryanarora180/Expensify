package com.example.expensify.viewexpense

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expensify.helper.FirestoreExpense
import com.example.expensify.helper.FirestoreRepository
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class ViewExpenseViewModel(application: Application) : ViewModel() {

    private val repository = FirestoreRepository(application)

    private val formatter = SimpleDateFormat("MMMM dd", Locale.getDefault())

    fun formatAmount(double: Double): String {
        return if (double >= 0.0) {
            "₹${NumberFormat.getNumberInstance(Locale.getDefault()).format(double)}"
        } else {
            "-₹${NumberFormat.getNumberInstance(Locale.getDefault()).format(abs(double))}"
        }
    }

    fun formatDate(dateToFormat: Date): String {
        return formatter.format(dateToFormat)
    }

    fun deleteExpense(expense: FirestoreExpense) {
        repository.deleteExpense(expense)
    }

}

class ViewExpenseViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ViewExpenseViewModel(
            application
        ) as T
    }
}
