package com.example.mad_ca1_27.activities

//all the compose imports

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mad_ca1_27.main.AppData
import com.example.mad_ca1_27.models.Transaction


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       setContent {
           FinanceTrackerScreen()
       }

    }


}
@Composable
fun FinanceTrackerScreen() {

    val transactions = AppData.transactions.findAll()

    val income = transactions
        .filter { it.isIncome}
        .sumOf { it.amount}


    val expenses = transactions
        .filter { !it.isIncome}
        .sumOf {it.amount}

    val balance = income - expenses


    Scaffold(
        topBar = {
            Text(
                text = "Finance Tracker",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp)
            )
        }
    ) {paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),

        ){

            Text(
                text = "Balance",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "€%.2f".format(balance),
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ){
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("Income")
                        Text(
                            "€%.2f".format(income),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("Expenses")
                        Text(
                            "€%.2f".format(expenses),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Recent Transactions",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (transactions.isEmpty()) {

                Text(
                    text = "No transactions yet"
                )
            }else {
                transactions.takeLast(5).forEach { transaction ->
                    TransactionRow(transaction)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    //to be added
                }, modifier = Modifier.padding(top = 24.dp)
            ){
                Text("Add Transaction")
            }
        }
    }

}

@Composable
fun TransactionRow(transaction: Transaction) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {

                Text(
                    text = transaction.title,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = transaction.category
                )
            }

            Text(
                text = if(transaction.isIncome) {
                    "+ €%.2f".format(transaction.amount)
                }else{
                    "- €%.2f".format(transaction.amount)
                }
            )
        }
    }
}