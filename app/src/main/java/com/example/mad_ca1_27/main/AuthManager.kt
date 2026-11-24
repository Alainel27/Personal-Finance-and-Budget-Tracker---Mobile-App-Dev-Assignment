package com.example.mad_ca1_27.main

import android.content.Context
import android.content.SharedPreferences
import java.security.MessageDigest

//hanldes the authentication of the app
object AuthManager{
    //private so only code from this class can access it
    //lateinit so it will be initialised later rather than immediately
    //sharedpreferences android storage that saves info as key-value pairs
    private lateinit var prefs: SharedPreferences

    //function sets up the storage system
    fun initialise(context: Context){
        //retreives the sharedPref file called auth
        prefs = context.applicationContext.getSharedPreferences("auth", Context.MODE_PRIVATE)
    }

    //gets the currently logged user
    val currentUser: String?
        get() = prefs.getString("current_user", null)


    //signing up new users
    // gets the email and password
    fun signUp(email: String, password: String): String?{
        //storage key to identify stored password
        val key = "user_" + email.trim().lowercase()
        //checks if the account exists or not
        if (prefs.contains(key)) return "An account with this email already exists"

        //saves the account
        prefs.edit()
            .putString(key, hash(password))
            .putString("current_user", email.trim().lowercase())
            .apply()
        return null
    }

    //logging in checks whether the email and password are the same
    fun logIn(email: String, password: String): String? {
        //generates the same key so it finds the same key
        val key = "user_" + email.trim().lowercase()
        //retreives the same password hash
        val saved = prefs.getString(key, null)
        //validation if there are no stored passwords or if the password does not match
        if (saved == null || saved != hash(password)) return "Incorrect email or password"
        prefs.edit().putString("current_user", email.trim().lowercase()).apply()
        return null
    }

    //logs out the user
    fun logOut() {
        prefs.edit().remove("current_user").apply()
    }

    //logs out a user
    private fun hash(password: String): String{
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it)}
    }


}