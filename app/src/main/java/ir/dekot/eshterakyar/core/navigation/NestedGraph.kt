package ir.dekot.eshterakyar.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import ir.dekot.eshterakyar.core.themePreferences.ThemeViewModel
import ir.dekot.eshterakyar.core.utils.LocalTheme
import ir.dekot.eshterakyar.feature_home.presentation.screen.HomeScreen
import ir.dekot.eshterakyar.screens.AddSubscriptionScreen
import ir.dekot.eshterakyar.screens.ProfileScreen
import ir.dekot.eshterakyar.screens.ReportsScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun NestedGraph() {
    // DEBUG: Log to validate ViewModel instantiation issue
    println("DEBUG: NestedGraph - Attempting to create ThemeViewModel")
    val viewModel: ThemeViewModel = koinViewModel()
    println("DEBUG: NestedGraph - ThemeViewModel created successfully")
    val isDark by viewModel.isDarkTheme.collectAsStateWithLifecycle()
    val theme = LocalTheme.current
    val backStack = rememberNavBackStack<BottomBarItem>(BottomBarItem.Home)
    Box(modifier = Modifier.fillMaxSize()) {
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryDecorators = listOf(
                rememberSavedStateNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry<BottomBarItem.Home> { HomeScreen(backStack = backStack) }
                entry<BottomBarItem.AddSubscription> { AddSubscriptionScreen() }
                entry<BottomBarItem.Reports> { ReportsScreen() }
                entry<BottomBarItem.Profile> { ProfileScreen() }

            }
        )

        // Bottom Bar شناور بالای محتوا
        AnimatedNavigationBar(
            backStack =backStack,
            modifier = Modifier.align(Alignment.BottomCenter),
            barColor =theme.barColor,
            circleColor = if (isDark) Color.White.copy(alpha = 0.2f) else  Color(0xFFadb5bd),
            selectedColor = theme.bottomBarSelectedItemColor,
            unselectedColor = theme.bottomBarUnselectedItemColor
        )
    }
}