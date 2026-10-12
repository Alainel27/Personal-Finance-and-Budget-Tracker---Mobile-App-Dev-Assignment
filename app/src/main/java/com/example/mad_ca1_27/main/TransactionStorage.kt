package com.example.mad_ca1_27.main

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import com.example.mad_ca1_27.models.Transaction
import java.io.File

class TransactionStorage(context: Context) {

    private val file = File(context.filesDir, "transaction.json")

    fun save(transactions : List<Transaction>){
        val json = JSONArray()

        for (t in transactions){
            json.put(
                JSONObject().apply{
                    put("id", t.id)
                    put("title", t.title)
                    put("amount", t.amount)
                    put("category", t.category)
                    put("description", t.description)
                    put("date", t.date)
                    put("isIncome", t.isIncome)
                }
            )
        }

        file.writeText(json.toString())

    }

    fun load(): List<Transaction> {
        if (!file.exists()) return emptyList()

        val json = JSONArray(file.readText())

        return (0 until json.length()).map { i ->
            val t = json.getJSONObject(i)

            Transaction(
                id = t.getLong("id"),
                title = t.getString("title"),
                amount = t.getDouble("amount"),
                category = t.getString("category"),
                description = t.getString("description"),
                date = t.getString("date"),
                isIncome = t.getBoolean("isIncome")
            )
        }
    }

}