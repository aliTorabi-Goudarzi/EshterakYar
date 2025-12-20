package ir.dekot.eshterakyar.core.di

import ir.dekot.eshterakyar.core.navigation.NestedGraph
import ir.dekot.eshterakyar.core.navigation.RootNavigator
import ir.dekot.eshterakyar.core.navigation.Screens
import ir.dekot.eshterakyar.feature_home.presentation.screen.EditSubscriptionScreen
import ir.dekot.eshterakyar.feature_home.presentation.screen.SubscriptionDetailScreen
import ir.dekot.eshterakyar.screens.ProfileDetailScreen
import ir.dekot.eshterakyar.screens.SettingsScreen
import ir.dekot.eshterakyar.screens.personalInfo.screen.PersonalInformationRoute
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(KoinExperimentalAPI::class)
val rootNavigationModule = module {
    single { RootNavigator(startDestination = Screens.NestedGraph) }

    navigation<Screens.NestedGraph> {
        NestedGraph()
    }

    navigation<Screens.SubscriptionDetail> { route ->
        SubscriptionDetailScreen(
            subscriptionId = route.subscriptionId,
        )
    }

    navigation<Screens.EditSubscription> { route ->
        EditSubscriptionScreen(
            subscriptionId = route.subscriptionId,
        )
    }

    navigation<Screens.SettingsScreen> {
        SettingsScreen(rootNavigator = get<RootNavigator>())
    }

        navigation<Screens.ProfileDetailScreen> {

            ProfileDetailScreen()

        }

    

        navigation<Screens.PersonalInformation> {

            val rootNavigator: RootNavigator = get()

            PersonalInformationRoute(

                onNavigateBack = { rootNavigator.goBack() }

            )

        }

    

    }

    