package com.example.expensify.viewexpense

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expensify.R
import com.example.expensify.helper.FirestoreExpense
import com.example.expensify.helper.FirestoreRepository
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class ViewExpenseViewModel(
    application: Application,
    expense: FirestoreExpense
) :
    ViewModel() {

    private val repository = FirestoreRepository(application)

    private val formatter =
        SimpleDateFormat(application.getString(R.string.date_format), Locale.getDefault())

    val formattedAmount =
        "${application.getString(R.string.inr_symbol)}${NumberFormat.getNumberInstance(Locale.getDefault())
            .format(abs(expense.amount))}"
    val isIncome = expense.amount >= 0.0
    val type =
        if (isIncome) application.getString(R.string.income_no_caps) else application.getString(R.string.expense_no_caps)
    val formattedDate =
        "${application.getString(R.string.an)} $type ${application.getString(R.string.on)} ${formatter.format(
            expense.date
        )}"

    fun deleteExpense(expense: FirestoreExpense) {
        repository.deleteExpense(expense)
    }

}

class ViewExpenseViewModelFactory(
    private val application: Application,
    private val expense: FirestoreExpense
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ViewExpenseViewModel(
            application,
            expense
        ) as T
    }
}
