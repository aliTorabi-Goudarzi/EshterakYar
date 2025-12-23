package ir.dekot.eshterakyar.core.di

import ir.dekot.eshterakyar.core.navigation.BottomBarItem
import ir.dekot.eshterakyar.core.navigation.NestedNavigator
import ir.dekot.eshterakyar.core.navigation.RootNavigator
import ir.dekot.eshterakyar.core.navigation.Screens
import ir.dekot.eshterakyar.feature_home.presentation.screen.HomeRoute
import ir.dekot.eshterakyar.screens.AddSubscriptionScreen
import ir.dekot.eshterakyar.screens.ProfileScreen
import ir.dekot.eshterakyar.screens.ReportsScreen
import org.koin.compose.koinInject
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(KoinExperimentalAPI::class)
val nestedNavigationModule = module {
    single { NestedNavigator(startDestination = BottomBarItem.Home) }

    navigation<BottomBarItem.Home> {
        val nestedNavigator: NestedNavigator = koinInject()
        val rootNavigator: RootNavigator = koinInject()
        
        HomeRoute(
            onNavigateToAddSubscription = { nestedNavigator.navigateTo(BottomBarItem.AddSubscription) },
            onNavigateToDetail = { id -> rootNavigator.navigateTo(Screens.SubscriptionDetail(id)) },
            onNavigateToEdit = { id -> rootNavigator.navigateTo(Screens.EditSubscription(id)) }
        )
    }
    navigation<BottomBarItem.AddSubscription> { 
        val rootNavigator: RootNavigator = koinInject()
        AddSubscriptionScreen(
            onEditSubscription = { id -> rootNavigator.navigateTo(Screens.EditSubscription(id)) }
        ) 
    }
    navigation<BottomBarItem.Reports> { ReportsScreen() }
    navigation<BottomBarItem.Profile> { ProfileScreen()}
}
