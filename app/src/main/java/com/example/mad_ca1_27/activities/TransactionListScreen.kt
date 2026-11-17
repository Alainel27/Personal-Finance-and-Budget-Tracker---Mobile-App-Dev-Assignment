package com.example.mad_ca1_27.activities

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.mad_ca1_27.main.AppData
import androidx.compose.ui.Alignment


@Composable
fun TransactionListScreen(){
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var refreshKey by remember { mutableStateOf(0) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver {_, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                refreshKey++
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    var selectedCategory by remember { mutableStateOf("All") }

    var showDeleteAllDialog by remember { mutableStateOf(false) }

    val allTransactions = remember ( refreshKey) {
        AppData.transactions.findAll().reversed()
    }


    val categories = listOf("All") + allTransactions.map { it.category }.distinct()

    val activeCategory = if(selectedCategory in categories) selectedCategory else "All"

    val transactions = if(activeCategory == "All"){
        allTransactions
    } else{
        allTransactions.filter { it.category == activeCategory }
    }


    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
        Text(
            text= "All Transactions",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
            )
            if (allTransactions.isNotEmpty()) {
                TextButton(onClick = {
                    showDeleteAllDialog = true
                }) {
                    Text("Delete All")
                }
            }
        }

        }
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            if (allTransactions.isNotEmpty()){
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    items(categories) { category ->
                        FilterChip(
                            selected = category ==activeCategory,
                            onClick = {selectedCategory = category},
                            label = {Text(category)}

                        )
                    }
                }
            }
            if(transactions.isEmpty()) {
                Text("No Transaction Found")
            }else{
                //lazy column only composes and renders items currently visible so it makes it smoother
                LazyColumn {
                    items(transactions, key= { it.id}) {transaction ->

                        TransactionRow(
                            title = transaction.title,
                            category = transaction.category,
                            amount = transaction.amount,
                            isIncome = transaction.isIncome,
                            onEdit = {
                                val intent = Intent(context, AddEditTransactionActivity::class.java)
                                intent.putExtra("TRANSACTION_ID", transaction.id)
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            }
        }
    }

    if (showDeleteAllDialog) {
        AlertDialog(
            onDismissRequest = {showDeleteAllDialog = false},
            title = { Text("Delete all transactions?") },
            text = {Text("Deletes all of the transactions and cannot be undone")},
            confirmButton = {
                TextButton(onClick ={
                    AppData.transactions.deleteAll()

                    AppData.saveTransaction()

                    refreshKey++
                    showDeleteAllDialog = false

                }) {Text("Delete All")}
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAllDialog = false}) { Text("Cancel") }
            }
        )
    }
}