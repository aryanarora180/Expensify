package com.example.expensify

import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.widget.TextView

class MainActivity : WearableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Enables Always-on
        setAmbientEnabled()

        val balance = findViewById<TextView>(R.id.balance_amount_text)
        balance.text = "50,000"
    }
}
