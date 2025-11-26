package ir.dekot.eshterakyar.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import ir.dekot.eshterakyar.core.themePreferences.ThemeViewModel
import ir.dekot.eshterakyar.core.utils.LocalTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.compose.navigation3.koinEntryProvider
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun NestedGraph(
) {
    val viewModel: ThemeViewModel = koinViewModel()
    val isDark by viewModel.isDarkTheme.collectAsStateWithLifecycle()
    val theme = LocalTheme.current
    val entryProvider = koinEntryProvider()
    val nestedNavigator = koinInject<NestedNavigator>()
    Box(modifier = Modifier.fillMaxSize()) {
        NavDisplay(
            backStack = nestedNavigator.backStack,
            onBack = { nestedNavigator.goBack() },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider
        )

        // Bottom Bar شناور بالای محتوا
        AnimatedNavigationBar(
            navigateTo = {route -> nestedNavigator.navigateTo(route)},
            modifier = Modifier.align(Alignment.BottomCenter),
            barColor =theme.barColor,
            circleColor = if (isDark) Color.White.copy(alpha = 0.2f) else  Color(0xFFadb5bd),
            selectedColor = theme.bottomBarSelectedItemColor,
            unselectedColor = theme.bottomBarUnselectedItemColor,
            nestedNavigator = nestedNavigator
        )
    }
}