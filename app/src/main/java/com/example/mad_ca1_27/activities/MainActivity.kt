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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mad_ca1_27.main.AppData
import com.example.mad_ca1_27.models.Transaction
import android.content.Intent
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.lifecycle.compose.LocalLifecycleOwner


//declared as main activity
//jetpack compose is used
//Ai was used to help understand Jetpack Compose and designing the site along with the jetpack compose site.
//AI was used throughout the project the development of the project to help me understand and create individual features and to further understand Kotlin
//and Jetpack Compose code. It was also used to troubleshoot and help with errors.
class MainActivity : ComponentActivity() {
    //main activity set up
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppData.initialise(applicationContext)
        //sets up screen interface
       setContent {
           //shows the dashboard
           FinanceTrackerScreen()
       }

    }


}
//the function dashboard that dispalys the balance, income, expenses, recent transactions and add transaction button along with edit button
@Composable
fun FinanceTrackerScreen() {
    //declares the varaible that cannot be reassigned
    //context is used in AddEdit when a user wants to add oir edit a transaction
    val context = LocalContext.current

    //lifestyle tracks the state of a screen
    //its purpose is to detect when the user returns to the dashboard after visiting another screen
    val lifecycleOwner = LocalLifecycleOwner.current

    //this variable is a signal to refresh the dashboard, when its value changes, compose knows to recompose the UI
    var refreshKey by remember { mutableStateOf(0) }

    //AI was used to help in understanding and creating this code
    //The code is vital as it used in detecting when the dashboard becomes active and triggers a refresh
    //registers a lifecycle observer and remove it
    DisposableEffect(lifecycleOwner) {
        //watches for the lifecycle changes so the app cna respond when the dashboard becomes active
        val observer = LifecycleEventObserver { _, event ->

            //checks if the dashboard is in a resumed state and if it is the refreshkey increments
            //which in turn refreshes the dashbaord
            if (event == Lifecycle.Event.ON_RESUME) {
                refreshKey++
            }
        }
        //this line connects it to the lifecycle so it can receive events

        lifecycleOwner.lifecycle.addObserver(observer)

        //prevents the observer from remaining when unneeded
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    //holds the list of transactions that the dashbaord uses
    val transactions = remember (refreshKey) {
        AppData.transactions.findAll()
    }

    //calculates the total income
    val income = transactions
        .filter { it.isIncome}
        .sumOf { it.amount}


    val expenses = transactions
        .filter { !it.isIncome}
        .sumOf {it.amount}

    //calculates the values together to get the balance
    val balance = income - expenses



    //structure for the screen
    Scaffold(
        topBar = {
            Text(
                //heading displayed
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

            //heading for the balnce
            Text(
                text = "Balance",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                //displays it in euro
                //formatting instructions to .2 decimal places and f indicates a floating point number, and inserts it in the balance
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
                //displays text as heading
                text = "Recent Transactions",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            //checks if the transactions exist
            if (transactions.isEmpty()) {

                //if there is none then it will return none
                Text(
                    text = "No transactions yet"
                )
                //if there is a transaction
            }else {
                //takes the last 5 in the list
                transactions.takeLast(5).forEach { transaction ->
                    TransactionRow(
                        //transaction information displayed
                        title = transaction.title,
                        category = transaction.category,
                        amount = transaction.amount,
                        isIncome = transaction.isIncome,
                        //if a user clicks edit callback
                        onEdit = {
                            //the action the android app should perform
                            val intent = Intent(
                                context,
                                //the activity that should be open
                                AddEditTransactionActivity::class.java

                            )
                            //attiondal info to the intent
                            //the key used to identify the info
                            intent.putExtra("TRANSACTION_ID", transaction.id)

                            //opens the AddEditTransactionActivity
                            context.startActivity(intent)


                        }


                        )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            //button used to access the other activity for creating transactions
            //same idea as the callback previously
            Button(
                onClick = {
                    val intent = Intent(
                        context,
                        AddEditTransactionActivity::class.java
                    )
                    context.startActivity(intent)
                },
                modifier = Modifier.padding(top = 24.dp)
            ){
                Text("Add Transaction")
            }
        }
    }

}

//function for the cards
@Composable
fun TransactionRow(
    //the parameters
    title: String,
    category: String,
    amount: Double,
    isIncome: Boolean,
    //function to run when user clicks edit
    onEdit: () -> Unit

) {


    //cards are created as it is more visually appealing
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column (
                modifier = Modifier.weight(1f)

            ){
                //the details of the transaction
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = category
                )

                //if it is income or expense and displays
                Text(
                    text = if(isIncome) {
                        "+ €%.2f".format(amount)
                    }else{
                        "- €%.2f".format(amount)
                    }
                )

            }

            //executes the callback passed
            Button(
                onClick = onEdit
            ) {
                Text("Edit")
            }

        }
    }
}