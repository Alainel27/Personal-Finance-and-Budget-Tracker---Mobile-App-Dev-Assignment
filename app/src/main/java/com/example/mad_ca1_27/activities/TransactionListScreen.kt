package com.example.mad_ca1_27.activities

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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

    val transactions = remember ( refreshKey ) {
        AppData.transactions.findAll().reversed()
    }
    Scaffold(
        topBar = {
        Text(
            text= "All Transactions",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
            )
        }
    ){paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            if(transactions.isEmpty()) {
                Text("No Transaction Yet")
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
}