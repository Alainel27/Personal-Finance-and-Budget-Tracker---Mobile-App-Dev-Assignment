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


object Routes {
    const val SPLASH = "splash"
    const val HOME = "home"
    const val TRANSACTIONS = "transactions"

    const val LOGIN = "login"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.SPLASH){
        composable ( Routes.SPLASH ){
            SplashScreen(
                onFinished = {
                   val destination = if(AuthManager.currentUser != null){
                       Routes.HOME
                   }else{
                       Routes.LOGIN
                   }
                    navController.navigate(destination) {
                        popUpTo(Routes.SPLASH) { inclusive = true}
                    }
                }
            )
        }
        composable (Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) {inclusive = true}
                    }
                }
            )
        }
        composable(Routes.HOME){
            FinanceTrackerScreen (
                onViewAll = {navController.navigate(Routes.TRANSACTIONS)},
                onLogout = {
                    AuthManager.logOut()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) {inclusive = true}
                    }
                }
            )
        }
        composable ( Routes.TRANSACTIONS ){
            TransactionListScreen()
        }
    }
}

