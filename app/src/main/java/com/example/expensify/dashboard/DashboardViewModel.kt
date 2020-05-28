package com.example.expensify.dashboard

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expensify.helper.FirestoreExpense
import com.example.expensify.helper.FirestoreRepository
import java.text.NumberFormat
import java.util.*
import kotlin.math.abs

class DashboardViewModel(application: Application) : ViewModel() {

    private val TAG = "DashboardViewModel"

    private val repository = FirestoreRepository(application)
    private var expensesData: MutableLiveData<List<FirestoreExpense>> = MutableLiveData()

    var totalBalance: MutableLiveData<Double> = MutableLiveData(0.0)
    var totalIncome: MutableLiveData<Double> = MutableLiveData(0.0)
    var totalExpenses: MutableLiveData<Double> = MutableLiveData(0.0)

    fun getExpenses(): LiveData<List<FirestoreExpense>> {
        repository.getExpenses().addSnapshotListener { documents, e ->
            if (e != null) {
                Log.e(TAG, "Expenses listening failed", e)
                expensesData.value = null
                return@addSnapshotListener
            }

            val expensesList: MutableList<FirestoreExpense> = mutableListOf()
            var balance = 0.0
            var income = 0.0
            var expenses = 0.0

            documents?.forEach { document ->
                val amount = document.getDouble("amount")!!

                balance += amount
                if (amount >= 0.0)
                    income += amount
                else
                    expenses += abs(amount)

                totalBalance.value = balance
                totalIncome.value = income
                totalExpenses.value = expenses

                expensesList.add(
                    FirestoreExpense(
                        document.id,
                        amount,
                        document.getString("title")!!,
                        document.getString("description") ?: "",
                        document.getGeoPoint("location"),
                        document.getDate("date")!!
                    )
                )
            }
            expensesData.value = expensesList
        }
        return expensesData
    }

    fun formatAmount(double: Double): String {
        return if (double >= 0.0) {
            "₹${NumberFormat.getNumberInstance(Locale.getDefault()).format(double)}"
        } else {
            "-₹${NumberFormat.getNumberInstance(Locale.getDefault()).format(abs(double))}"
        }
    }

}

class DashboardViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DashboardViewModel(
            application
        ) as T
    }
}