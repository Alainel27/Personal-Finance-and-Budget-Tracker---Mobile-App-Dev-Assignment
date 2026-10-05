package com.example.mad_ca1_27.models

import java.util.concurrent.atomic.AtomicLong

class TransactionList {
    //CRUD basics
    private val transactions = ArrayList<Transaction>()

    private var lastId = AtomicLong(0L)

    fun findAll(): List<Transaction> {
        return transactions
    }

    fun findOne(id: Long): Transaction? {
        return transactions.find { it.id == id}
    }

    fun create(transaction: Transaction) {
        transaction.id = lastId.incrementAndGet()
        transactions.add(transaction)
    }

    fun update(transaction: Transaction): Boolean{
        val foundTransaction = findOne(transaction.id)

        return if(foundTransaction != null) {
            foundTransaction.title = transaction.title
            foundTransaction.amount = transaction.amount
            foundTransaction.category = transaction.category
            foundTransaction.description = transaction.description
            foundTransaction.date = transaction.date
            foundTransaction.isIncome = transaction.isIncome
            true
        }else{
            false
        }

    }

    fun delete(id: Long): Boolean {
        return transactions.removeIf{it.id == id}
    }


}