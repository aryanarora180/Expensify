package com.example.expensify.viewexpense

import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class ViewExpenseViewModel : ViewModel() {

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

}
