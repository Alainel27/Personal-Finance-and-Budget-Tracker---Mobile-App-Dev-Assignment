package com.example.mad_ca1_27.activities

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.mad_ca1_27.main.AuthManager

//This class creates the login and registration screen for the app
//fun for the loginscreen
@Composable
//onLoginSuccess callback function action provided that happens when the login or signup is successful(it will route to the Home page)
fun LoginScreen(onLoginSuccess: () -> Unit) {
    //variables store information relating to the login
    //stores email info
    //remeber so that compose remains in the composition
    //mutableStateOf so that if the value changes than the ui is updated
    var email by remember { mutableStateOf("") }
    //stores the password
    var password by remember { mutableStateOf("") }
    //stores the error message
    var errorMessage by remember { mutableStateOf("") }

    //this function acts as validation
    fun inputsValid(): Boolean {
        //checks if the vars is empty
        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Enter your email and password"
            return false
        }
        //it also checks if the password is longer than 6 characters
        if (password.length < 6 ) {
            errorMessage = "Password must be at least 6 characters"
            return false
        }
        return true
    }

    //the screen layout

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //heading
        Text(
            text = "Finance Tracker",
            style = MaterialTheme.typography.headlineLarge
        )
        //login/register subtitle
        Text(
            text = "Log in or create an account",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            //email var
            value = email,
            //runs when the user changes the text
            onValueChange = {
                email = it
                errorMessage = ""
            },
            //displays the email text field
            label = {Text("Email")},
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        //same as before
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                errorMessage = ""
            },
            label = {Text("Password")},
            singleLine = true,
            //censored password
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()

        )

        //display error message
        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        //signup button
        Button(
            onClick = {
                if (inputsValid()) {
                    //
                    val error = AuthManager.logIn(email, password)
                    if (error == null) onLoginSuccess() else errorMessage = error
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Log In")
        }

        Spacer(modifier = Modifier.height(12.dp))

        //signup button
        OutlinedButton(
            onClick = {
                if (inputsValid()) {
                    val error = AuthManager.signUp(email, password)
                    if (error == null) onLoginSuccess() else errorMessage = error
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Up")
        }




    }
}