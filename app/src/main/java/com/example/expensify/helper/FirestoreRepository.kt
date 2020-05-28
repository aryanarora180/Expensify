package com.example.expensify.helper

import android.app.Application
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreRepository(application: Application) {

    private val firestoreDB = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    fun saveExpense(expense: Expense): Task<DocumentReference> {
        return firestoreDB.collection("Users").document(userId!!).collection("Expenses")
            .add(expense)
    }

    fun getExpenses(): CollectionReference {
        return firestoreDB.collection("Users/$userId/Expenses")
    }

    fun deleteExpense(firestoreExpense: FirestoreExpense): Task<Void> {
        return firestoreDB.collection("Users/$userId/Expenses").document(firestoreExpense.docId)
            .delete()
    }

}