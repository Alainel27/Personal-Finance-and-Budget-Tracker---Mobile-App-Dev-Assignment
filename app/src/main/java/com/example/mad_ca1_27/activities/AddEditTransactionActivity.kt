package com.example.mad_ca1_27.activities
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.material3.Button
import com.example.mad_ca1_27.main.AppData
import com.example.mad_ca1_27.models.Transaction
import android.app.Activity
import androidx.compose.ui.platform.LocalContext



class AddEditTransactionActivity : ComponentActivity() {
    override  fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        setContent {
            AddEditTransactionScreen()
        }
    }
}

@androidx.compose.runtime.Composable
fun AddEditTransactionScreen() {

    val context = LocalContext.current

    val transactionId = (context as? Activity)?.intent?.getLongExtra(
        "TRANSACTION_ID",
        -1L
    ) ?: -1L

    val existingTransaction = if (transactionId != -1L) {
        AppData.transactions.findOne(transactionId)
    }else{
        null
    }

    val isEditing = existingTransaction != null

    Scaffold(
        topBar = {
            Text(
                text = if (isEditing) "Edit Transaction" else "Add Transaction",
                modifier = Modifier.padding(16.dp)
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            var title by remember {
                mutableStateOf(existingTransaction?.title ?: "")
            }

            var amount by remember {
                mutableStateOf(existingTransaction?.amount?.toString() ?: "")
            }

            var category by remember {
                mutableStateOf(existingTransaction?.category ?: "")

            }
            var description by remember {
                mutableStateOf(existingTransaction?.description ?: "")

            }

            var isIncome by remember {
                mutableStateOf(existingTransaction?.isIncome ?: false)
            }

            var titleError by remember { mutableStateOf(false) }

            var amountError by remember { mutableStateOf(false) }

            var categoryError by remember { mutableStateOf(false) }



            Text(
                text = "Transaction Details",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    titleError = false
                },
                label = {Text("Title")},
                isError = titleError,
                modifier = Modifier.fillMaxWidth()

            )

            if (titleError) {
                Text("Title is required")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = {
                    amount = it
                    amountError = false
                },
                label = {Text("Amount")},
                isError = amountError,
                modifier = Modifier.fillMaxWidth()

            )
            if (amountError) {
                Text("Enter an amount that is greater than zero")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = category,
                onValueChange = {
                    category = it
                    categoryError = false
                },
                label = {Text("Category")},
                isError = categoryError,
                modifier = Modifier.fillMaxWidth()
            )

            if (categoryError){
                Text("Category is required")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = {description = it},
                label = {Text("Description")},
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Transaction Type",
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = !isIncome,
                        onClick = {isIncome = false}
                    )
                    Text("Expense")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = isIncome,
                        onClick = {isIncome = true}
                    )
                    Text("Income")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                   val transactionAmount = amount.toDoubleOrNull()

                    titleError = title.isBlank()
                    amountError = transactionAmount == null || transactionAmount <- 0
                    categoryError = category.isBlank()

                    if (!titleError && !amountError && !categoryError) {
                        val transaction = Transaction(
                            title = title.trim(),
                            amount = transactionAmount!!,
                            category = category.trim(),
                            description = description.trim(),
                            isIncome = isIncome
                        )

                        if (isEditing) {
                            transaction.id = transactionId
                            AppData.transactions.update(transaction)
                        }else{
                            AppData.transactions.create(transaction)
                        }
                        (context as? Activity)?.finish()
                    }
                },
                modifier = Modifier.fillMaxWidth()

            ) {
                Text(if (isEditing) "Update Transaction" else "Save Transaction")
            }
            if (isEditing) {
                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        AppData.transactions.delete(transactionId)
                        (context as? Activity)?.finish()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Delete Transaction")
                }
            }

        }
    }
}