package ir.dekot.eshterakyar.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import ir.dekot.eshterakyar.feature_home.presentation.screen.SubscriptionDetailScreen
import ir.dekot.eshterakyar.feature_home.presentation.screen.EditSubscriptionScreen

@Composable
fun RootGraph(padding : PaddingValues) {
    val backStack = rememberNavBackStack<Screens>(Screens.NestedGraph)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<Screens.NestedGraph> {
                NestedGraph(
                )
            }
            entry<Screens.SubscriptionDetail> { screen ->
                SubscriptionDetailScreen(
                    subscriptionId = screen.subscriptionId,
                    backStack = backStack
                )
            }
            entry<Screens.EditSubscription> { screen ->
                EditSubscriptionScreen(
                    subscriptionId = screen.subscriptionId,
                    backStack = backStack
                )
            }
        }
    )
}