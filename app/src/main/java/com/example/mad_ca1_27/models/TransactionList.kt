package com.example.mad_ca1_27.models

import java.util.concurrent.atomic.AtomicLong

class TransactionList {
    //CRUD basics
    //contains the transaction details in an empty list
    private val transactions = ArrayList<Transaction>()


    private var lastId = AtomicLong(0L)

    fun findAll(): List<Transaction> {
        return transactions.toList()
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

    //JSON
    fun loadAll(savedTransactions: List<Transaction>) {
        transactions.clear()
        transactions.addAll(savedTransactions)

        lastId.set(savedTransactions.maxOfOrNull { it.id }?: 0L)
    }


}