package com.example.mad_ca1_27.main

import com.example.mad_ca1_27.models.Transaction
import com.example.mad_ca1_27.models.TransactionList
import android.content.Context

//manages the apps transaction data and connects with storage
object AppData {
    //declares transactions a new instance of TransactionList
    val transactions = TransactionList()

    //AI was used to help create and understand JSON storage
    //declares the object that handles saving and loading transactions
    private var storage: TransactionStorage? = null

    //checks if storage has been created
    fun initialise(context: Context) {
        if (storage == null) {
            //creates the storage handler
            storage = TransactionStorage(context.applicationContext)
            //restores saved data
            transactions.loadAll(storage!!.load())
        }
    }



    //saves the current transactions to persistent storage
    fun saveTransaction() {
        storage?.save(transactions.findAll())
    }

}


//Appdata.transactions.findAll()