package ir.dekot.eshterakyar.core.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ir.dekot.eshterakyar.core.themePreferences.ThemePreferences
import ir.dekot.eshterakyar.core.utils.LocalTheme
import org.koin.compose.koinInject

@Composable
fun EshterakYarApp() {
    val theme = LocalTheme.current
    val rootNavigator = koinInject<RootNavigator>()
    val themePreferences = koinInject<ThemePreferences>()

    // چک کردن اینکه آیا آنبوردینگ نشان داده شده
    LaunchedEffect(Unit) {
        val hasSeenOnboarding = themePreferences.getHasSeenOnboardingSync()
        if (!hasSeenOnboarding) {
            // اگر اولین بار است، به آنبوردینگ برو
            rootNavigator.navigateTo(Screens.Onboarding)
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(theme.backgroundColor)) {
        Scaffold(
                modifier = Modifier.fillMaxSize(), // کل صفحه رو پر کن
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                containerColor = Color.Transparent,
                bottomBar = {}
        ) { innerPadding -> RootGraph(padding = innerPadding) }
    }
}
