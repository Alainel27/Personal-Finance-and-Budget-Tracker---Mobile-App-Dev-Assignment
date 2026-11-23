package com.example.mad_ca1_27.main

import com.example.mad_ca1_27.models.Transaction
import com.example.mad_ca1_27.models.TransactionList
import android.content.Context
import android.util.Base64

//manages the apps transaction data and connects with storage
object AppData {
    //declares transactions a new instance of TransactionList
    val transactions = TransactionList()

    //AI was used to help create and understand JSON storage
    //declares the object that handles saving and loading transactions
    private var storage: TransactionStorage? = null
    private lateinit var appContext: Context

    //checks if storage has been created
    fun initialise(context: Context) {
        appContext = context.applicationContext

        AuthManager.currentUser?.let {loadForUser(it)}
    }

    fun loadForUser(email: String) {
        storage = TransactionStorage(appContext, fileNameFor(email))
        transactions.loadAll(storage!!.load())
    }

    fun clear() {
        storage = null
        transactions.loadAll(emptyList())
    }

    //saves the current transactions to persistent storage
    fun saveTransaction() {
        storage?.save(transactions.findAll())
    }

    private fun fileNameFor(email: String): String{
        val encoded = Base64.encodeToString(
            email.toByteArray(),
            Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
        )
        return "transactions_$encoded.json"
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

//CA2

//https://dev.to/elozino/getting-started-with-splash-screen-in-jetpack-compose-144l
//https://surajmyt.hashnode.dev/splash-screen-in-jetpack-compose
//https://www.geeksforgeeks.org/kotlin/animated-splash-screen-in-android-using-jetpack-compose/
//https://dribbble.com/tags/ft-logo-design
//https://dev.to/ethand91/android-jetpack-compose-filter-tutorial-21l9
//https://kotlinlang.org/docs/multiplatform/compose-navigation-routing.html#support-for-browser-navigation-in-web-apps
//https://github.com/osmdroid/osmdroid/wiki
//https://wiki.openstreetmap.org/wiki/Blocked_tiles