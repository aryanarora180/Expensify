package com.example.expensify.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expensify.R
import com.example.expensify.helper.FirestoreExpense
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class ExpenseAdapter(private val expensesList: List<FirestoreExpense>) :
    RecyclerView.Adapter<ExpenseAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(expense: FirestoreExpense) {
            val amountText: TextView = itemView.findViewById(R.id.expense_amount_text)
            val titleText: TextView = itemView.findViewById(R.id.expense_title_text)
            val dateText: TextView = itemView.findViewById(R.id.expense_date_text)

            amountText.text = formatAmount(expense.amount)
            titleText.text = expense.title
            dateText.text = formatDate(expense.date)
        }

        private fun formatAmount(double: Double): String {
            return if (double >= 0.0) {
                "₹${NumberFormat.getNumberInstance(Locale.getDefault()).format(double)}"
            } else {
                "-₹${NumberFormat.getNumberInstance(Locale.getDefault()).format(abs(double))}"
            }
        }

        private fun formatDate(dateToFormat: Date): String {
            return SimpleDateFormat("MMMM dd", Locale.getDefault()).format(dateToFormat)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_expense, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return expensesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(expensesList[position])
    }

}