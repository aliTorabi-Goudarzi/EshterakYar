package ir.dekot.eshterakyar.core.di

import ir.dekot.eshterakyar.core.navigation.BottomBarItem
import ir.dekot.eshterakyar.core.navigation.NestedNavigator
import ir.dekot.eshterakyar.feature_home.presentation.screen.HomeScreen
import ir.dekot.eshterakyar.screens.AddSubscriptionScreen
import ir.dekot.eshterakyar.screens.ProfileScreen
import ir.dekot.eshterakyar.screens.ReportsScreen
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(KoinExperimentalAPI::class)
val nestedNavigationModule = module {
    single { NestedNavigator(startDestination = BottomBarItem.Home) }

    navigation<BottomBarItem.Home> {
        HomeScreen()
    }
    navigation<BottomBarItem.AddSubscription> { AddSubscriptionScreen() }
    navigation<BottomBarItem.Reports> { ReportsScreen() }
    navigation<BottomBarItem.Profile> { ProfileScreen()}
}