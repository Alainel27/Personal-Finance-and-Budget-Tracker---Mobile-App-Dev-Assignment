//made separate class rather than putting it in main activity because it is more tidy and cleaner

package com.example.mad_ca1_27.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mad_ca1_27.activities.FinanceTrackerScreen
import com.example.mad_ca1_27.activities.LoginScreen
import com.example.mad_ca1_27.activities.SplashScreen
import com.example.mad_ca1_27.activities.TransactionListScreen
import com.example.mad_ca1_27.main.AuthManager
import com.example.mad_ca1_27.activities.MainActivity
import com.example.mad_ca1_27.activities.TransactionMapScreen
import com.example.mad_ca1_27.main.AppData


//controls the users moving to different screens
//Ai was used to help in the understanding and implementing of this code
object Routes {
    //creating the routes
    const val SPLASH = "splash"
    const val HOME = "home"
    const val TRANSACTIONS = "transactions"

    const val LOGIN = "login"
    const val MAP = "map"
}

//
@Composable
fun AppNavigation() {

    //navcontroller manages the navigation
    val navController = rememberNavController()

    //Navhost container displays the screen to the current navigation distination
    //the starting screen is the splash screen
    NavHost(navController = navController, startDestination = Routes.SPLASH){
        composable ( Routes.SPLASH ){
            //
            SplashScreen(
                onFinished = {
                    //when it is finished it check if someone is logged in
                    //if there is a user then it will bring it to home
                   val destination = if(AuthManager.currentUser != null){
                       Routes.HOME
                       //if there isnt then it will bring it to login screen
                   }else{
                       Routes.LOGIN
                   }
                    navController.navigate(destination) {
                        popUpTo(Routes.SPLASH) { inclusive = true}
                    }
                }
            )
        }
        //login navigation
        composable (Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    //calls appdata to load the info with an assocaited login user
                    AuthManager.currentUser?.let { AppData.loadForUser(it)}
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) {inclusive = true}
                    }
                }
            )
        }
        //home screen navigation
        composable(Routes.HOME){
            FinanceTrackerScreen (
                //callbacks with their respective button in the home screen
                //callback to viewall transactions
                onViewAll = {navController.navigate(Routes.TRANSACTIONS)},
                //callback to view the map
                onViewMap = {navController.navigate(Routes.MAP)},
                //callback to return to login and logout
                onLogout = {
                    //logouts the user
                    AuthManager.logOut()
                    //clears the loaded data
                    AppData.clear()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) {inclusive = true}
                    }
                }
            )
        }
        //allows it as an navigation destination when called
        composable ( Routes.TRANSACTIONS ){
            TransactionListScreen()
        }
        composable (Routes.MAP) {
            TransactionMapScreen()
        }
    }
}

