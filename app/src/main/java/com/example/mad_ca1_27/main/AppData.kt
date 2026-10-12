package com.example.mad_ca1_27.main

import com.example.mad_ca1_27.models.Transaction
import com.example.mad_ca1_27.models.TransactionList
import android.content.Context

object AppData {
    val transactions = TransactionList()

    private var storage: TransactionStorage? = null

    fun initialise(context: Context) {
        if (storage == null) {
            storage = TransactionStorage(context.applicationContext)
            transactions.loadAll(storage!!.load())
        }
    }

    fun saveTransaction() {
        storage?.save(transactions.findAll())
    }

}


//Appdata.transactions.findAll()