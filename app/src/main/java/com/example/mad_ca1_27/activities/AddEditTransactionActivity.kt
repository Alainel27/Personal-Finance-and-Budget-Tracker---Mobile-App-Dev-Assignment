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

    Scaffold(
        topBar = {
            Text(
                text = "Add Transaction",
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
            var title by remember { mutableStateOf("") }
            var amount by remember { mutableStateOf("") }
            var category by remember { mutableStateOf("") }
            var description by remember { mutableStateOf("") }
            var isIncome by remember { mutableStateOf(false) }

            Text(
                text = "Transaction Details",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = title,
                onValueChange = {title = it},
                label = {Text("Title")},
                modifier = Modifier.fillMaxWidth()

            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = {amount = it},
                label = {Text("Amount")},
                modifier = Modifier.fillMaxWidth()

            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = category,
                onValueChange = {category = it},
                label = {Text("Category")},
                modifier = Modifier.fillMaxWidth()
            )

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

                    if (title.isNotBlank() &&
                        transactionAmount != null &&
                        transactionAmount > 0 &&
                        category.isNotBlank()

                        ) {

                        val transaction = Transaction(

                            title = title,
                            amount = transactionAmount,
                            category = category,
                            description = description,
                            isIncome = isIncome

                        )

                        AppData.transactions.create(transaction)

                        (context as? Activity)?.finish()


                    }
                },
                modifier = Modifier.fillMaxWidth()

            ) {
                Text("Save Transaction")
            }

        }
    }
}