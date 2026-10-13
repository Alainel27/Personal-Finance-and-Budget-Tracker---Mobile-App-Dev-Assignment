package com.example.mad_ca1_27.models

//the data model for the one transaction
//the date isn't used yet
class Transaction (
    var id : Long = 0L,
    var title : String = "",
    var amount : Double = 0.0,
    var category: String = "",
    var description: String = "",
    var date: String = "",
    var isIncome: Boolean = false
)