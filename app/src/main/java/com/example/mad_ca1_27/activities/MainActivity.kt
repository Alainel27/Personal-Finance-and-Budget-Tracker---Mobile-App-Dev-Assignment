package com.example.mad_ca1_27.activities

//all the compose imports

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp



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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){

            Text(
                text = "Welcome to the Finance Tracker",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Track your Income,expenses and budget.",
                modifier = Modifier.padding(top = 8.dp)
            )

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