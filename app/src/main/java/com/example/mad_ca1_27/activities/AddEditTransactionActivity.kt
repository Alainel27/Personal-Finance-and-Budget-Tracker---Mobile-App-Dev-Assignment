
//imports
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


//provides the activity that displays the screen
class AddEditTransactionActivity : ComponentActivity() {
    //runs when the android creates the activity
    override  fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        //starts the jetpack compose content for the activity
        setContent {
            AddEditTransactionScreen()
        }
    }
}

//marks the function as a Compose function for the user interface
@androidx.compose.runtime.Composable
//build the display screen
fun AddEditTransactionScreen() {

    //creates the variable context which contains the android context
    val context = LocalContext.current

    //the variable holds the transaction ID
    //casts the context into an activity
    //accesses the activity intent only if the cast works
    val transactionId = (context as? Activity)?.intent?.getLongExtra(
        //the key used to retrieve the ID, the key matches the on used in MainActivity
        "TRANSACTION_ID",
        //default ID
        -1L
        //if the expression is null then the value is -1L
    ) ?: -1L

    //creates a variable to hold the existing transaction, the if checks if the ID is different from -1L
    val existingTransaction = if (transactionId != -1L) {
        //searches for the transaction with the matching ID
        AppData.transactions.findOne(transactionId)
    }else{
        //if there is nothing then the result is null
        null
    }

    //if the varaible is true if an existing transaction is found and false if not
    val isEditing = existingTransaction != null

    //screen structure
    Scaffold(
        topBar = {
            //Text for the heading
            Text(
                //creates the heading based on whether the transaction is being edited or created
                text = if (isEditing) "Edit Transaction" else "Add Transaction",
                //
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
            //the variables for the details
            //remember retains the value when the screen recomposes
            //without remember the state would be recreated during when it gets recomposed.
            var title by remember {
                //mutableStateOf returns a mutableState object which leads to recomposition in case the value changes and the value is read
                //existingTransaction?.title ?: "" accesses the title only if the existing Transaction is not null
                // the Elvis operator will return an empty string value when the expression is null, if it is not then it wont
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

            //variables used for errors
            //if the title is empty then it becomes true, vice versa

            var titleError by remember { mutableStateOf(false) }

            var amountError by remember { mutableStateOf(false) }

            var categoryError by remember { mutableStateOf(false) }



            //Transaction details heading
            //themes
            Text(
                text = "Transaction Details",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            //the title input field
            OutlinedTextField(
                //displays the title
                value = title,
                //handles the changes to the text field
                onValueChange = {
                    //updates to the new test the user types
                    title = it
                    //Clears the error when the user types
                    titleError = false
                },
                //Displays Title on the field label
                label = {Text("Title")},
                //if there is an error the error message will appear
                isError = titleError,
                //fills the remaining width space
                modifier = Modifier.fillMaxWidth()

            )

            //Title error
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

            //this text field doesn't have an error message
            OutlinedTextField(
                value = description,
                onValueChange = {description = it},
                label = {Text("Description")},
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            //Transaction Type Heading
            Text(
                text = "Transaction Type",
                style = MaterialTheme.typography.titleMedium
            )

            //horizontal layout
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    //expense Option if it is not income
                    RadioButton(
                        selected = !isIncome,
                        onClick = {isIncome = false}
                    )
                    Text("Expense")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    //option for income
                    RadioButton(
                        selected = isIncome,
                        onClick = {isIncome = true}
                    )
                    Text("Income")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            //button for saving or updating the transactions
            Button(
                onClick = {
                    //val so the variable cannot be reassigned
                    //converts the text in amount into a Double which can represent Decimals(makes it look cleaner)
                   val transactionAmount = amount.toDoubleOrNull()

                    //checks if it is blank
                    titleError = title.isBlank()
                    //checks if the amount is invalid
                    //checks if it was converted to a number
                    //checks if it is zero or less than 0
                    amountError = transactionAmount == null || transactionAmount <- 0
                    //checks if blank
                    categoryError = category.isBlank()

                    //the if statement checks if all the errors are null
                    if (!titleError && !amountError && !categoryError) {
                        //creates the transaction object
                        val transaction = Transaction(
                            title = title.trim(),
                            amount = transactionAmount!!,
                            category = category.trim(),
                            description = description.trim(),
                            isIncome = isIncome
                        )

                        //checks whether to add or update
                        if (isEditing) {
                            //copies the existing transaction ID into the new object
                            transaction.id = transactionId
                            //calls the update function to update the transaction
                            AppData.transactions.update(transaction)
                        }else{
                            //adds the new transaction to the list
                            AppData.transactions.create(transaction)
                        }
                        //saves the transaction list to JSON storage
                        AppData.saveTransaction()
                        //closes the activity
                        (context as? Activity)?.finish()
                    }
                },
                modifier = Modifier.fillMaxWidth()

                //either save or update based on if it is getting edited
            ) {
                Text(if (isEditing) "Update Transaction" else "Save Transaction")
            }
            //displaying the delete button to edited transactions
            if (isEditing) {
                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        //deletes the transaction
                        AppData.transactions.delete(transactionId)
                        //saves the deleted transaction to the JSON
                        AppData.saveTransaction()
                        //closes the activity
                        (context as? Activity)?.finish()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    //displays the text inside the button
                    Text("Delete Transaction")
                }
            }

        }
    }
}