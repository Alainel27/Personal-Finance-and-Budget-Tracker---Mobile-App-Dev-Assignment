//made separate class rather than putting it in main activity because it is more tidy and cleaner

package com.example.mad_ca1_27.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mad_ca1_27.activities.FinanceTrackerScreen
import com.example.mad_ca1_27.activities.SplashScreen
import com.example.mad_ca1_27.activities.TransactionListScreen


object Routes {
    const val SPLASH = "splash"
    const val HOME = "home"
    const val TRANSACTIONS = "transactions"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.SPLASH){
        composable ( Routes.SPLASH ){
            SplashScreen(
                onFinished = {
                    navController.navigate(Routes.HOME){

                    popUpTo(Routes.SPLASH) {inclusive = true}
                    }
                }
            )
        }
        composable (Routes.HOME) {
            FinanceTrackerScreen(
            onViewAll = {navController.navigate(Routes.TRANSACTIONS)})
        }
        composable ( Routes.TRANSACTIONS ){
            TransactionListScreen()
        }
    }
}

