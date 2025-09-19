package ir.dekot.eshterakyar.core.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ir.dekot.eshterakyar.screens.AddSubscriptionScreen
import ir.dekot.eshterakyar.feature_home.presentation.screen.HomeScreen
import ir.dekot.eshterakyar.screens.ProfileScreen
import ir.dekot.eshterakyar.screens.ReportsScreen

@Composable
fun NavHostScreen(navHostController : NavHostController,padding: PaddingValues){
    NavHost(
        navController = navHostController,
        startDestination = Screens.Home.route,
        modifier = Modifier.padding(padding)
    ) {
        composable(route = Screens.AddSubscription.route){
            AddSubscriptionScreen()
        }
        composable(route = Screens.Profile.route){
            ProfileScreen()
        }
        composable(route = Screens.Reports.route){
            ReportsScreen()
        }
        composable(route = Screens.Home.route){
            HomeScreen()
        }
    }
}