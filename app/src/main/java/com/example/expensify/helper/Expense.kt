package com.example.expensify.helper

import com.google.firebase.firestore.GeoPoint
import java.util.*

data class FirestoreExpense(
    val docId: String,
    val amount: Double,
    val merchant: String,
    val description: String = "",
    val geoPoint: GeoPoint?,
    val date: Date
)

data class Expense(
    val amount: Double,
    val merchant: String,
    val description: String = "",
    val geoPoint: GeoPoint?,
    val date: Date
)