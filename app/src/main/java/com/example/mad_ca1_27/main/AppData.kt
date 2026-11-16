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

//Sites used references
//https://composables.com/
//https://www.geeksforgeeks.org/kotlin/scaffold-in-android-using-jetpack-compose/
//https://developer.android.com/develop/ui/compose/modifiers
//https://joebirch.co/android/exporing-jetpack-compose-padding-modifier/
//https://www.tutorialkart.com/android-jetpack-compose/text-padding/
//https://androidengineers.substack.com/p/the-complete-internals-of-jetpack
//http://medium.com/@joseph.1hach/everything-you-need-to-know-about-remember-in-android-jetpack-compose-3fb14356365
//https://medium.com/@riyaspullurofficial/elvis-operator-in-kotlin-explained-simply-for-android-developers-c6a9380b0e32
//https://medium.com/@riztech.dev/theming-and-styling-in-jetpack-compose-with-materialtheme-c0c8254d8404
//https://foso.github.io/Jetpack-Compose-Playground/cookbook/textfield_changes/
//https://developer.android.com/topic/libraries/architecture/lifecycle
//https://proandroiddev.com/disposableeffect-side-effect-api-in-jetpack-compose-56cb2d3f7888