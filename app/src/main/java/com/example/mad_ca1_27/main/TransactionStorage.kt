package com.example.mad_ca1_27.main

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import com.example.mad_ca1_27.models.Transaction
import java.io.File

//Ai was used to help create and understand JSON storage in the project
class TransactionStorage(context: Context) {

    //the code identifies where the transaction will be stored
    private val file = File(context.filesDir, "transaction.json")

    //function for saving the transaction in storage
    fun save(transactions : List<Transaction>){
        //creates an empty JSON Array
        val json = JSONArray()

        //loops through the transactions in the list
        //and adds them into the JSON Object
        for (t in transactions){
            json.put(
                JSONObject().apply{
                    //converts the transactions into JSON
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

        //saves the JSON to the file
        file.writeText(json.toString())

    }

    //function to load the transactions
    fun load(): List<Transaction> {
        //checks if the file exists so it doesn't have to try read a nonexistent file
        if (!file.exists()) return emptyList()

        //reads the saved data and converts it into JSON array
        val json = JSONArray(file.readText())

        // loops through the transactions stored in the JSON and retrieves each transactions JSON object
        //json.length() returns the number of items in the JSON array
        //0 until json.length creates a range of indices
        //the map processes each index and collects the results into a new list
        return (0 until json.length()).map { i ->
            val t = json.getJSONObject(i)

            //Creates a kotlin object from the data stored in the JSON
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