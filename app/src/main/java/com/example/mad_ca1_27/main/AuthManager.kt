package com.example.mad_ca1_27.main

import android.content.Context
import android.content.SharedPreferences
import java.security.MessageDigest

object AuthManager{
    private lateinit var prefs: SharedPreferences

    fun initialise(context: Context){
        prefs = context.applicationContext.getSharedPreferences("auth", Context.MODE_PRIVATE)
    }

    val currentUser: String?
        get() = prefs.getString("current_user", null)


    fun signUp(email: String, password: String): String?{
        val key = "user_" + email.trim().lowercase()
        if (prefs.contains(key)) return "An account with this email already exists"

        prefs.edit()
            .putString(key, hash(password))
            .putString("current_user", email.trim().lowercase())
            .apply()
        return null
    }

    fun logIn(email: String, password: String): String? {
        val key = "user_" + email.trim().lowercase()
        val saved = prefs.getString(key, null)
        if (saved == null || saved != hash(password)) return "Incorrect email or password"
        prefs.edit().putString("current_user", email.trim().lowercase()).apply()
        return null
    }

    fun logOut() {
        prefs.edit().remove("current_user").apply()
    }

    private fun hash(password: String): String{
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it)}
    }


}