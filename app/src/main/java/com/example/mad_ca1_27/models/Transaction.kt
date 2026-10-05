package com.example.mad_ca1_27.models

class Transaction (
    var id : Long = 0L,
    var title : String = "",
    var amount : Double = 0.0,
    var category: String = "",
    var description: String = "",
    var date: String = "",
    var isIncome: Boolean = false
)