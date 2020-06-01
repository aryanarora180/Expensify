package com.example.expensify.helper

import androidx.annotation.Keep
import com.google.firebase.firestore.GeoPoint
import java.io.Serializable
import java.util.*

@Keep
data class FirestoreExpense(
    val docId: String,
    val amount: Double,
    val merchant: String,
    val description: String?,
    val geoPoint: GeoPoint?,
    val date: Date
) : Serializable

data class Expense(
    val amount: Double,
    val merchant: String,
    val description: String?,
    val geoPoint: GeoPoint?,
    val date: Date
)