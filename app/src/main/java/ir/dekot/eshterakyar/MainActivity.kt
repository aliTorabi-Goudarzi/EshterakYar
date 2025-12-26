package ir.dekot.eshterakyar

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.dekot.eshterakyar.core.navigation.EshterakYarApp
import ir.dekot.eshterakyar.core.theme.EshterakYarTheme
import ir.dekot.eshterakyar.core.themePreferences.ThemeMode
import ir.dekot.eshterakyar.core.themePreferences.ThemeViewModel
import ir.dekot.eshterakyar.core.utils.LocalTheme
import ir.dekot.eshterakyar.core.utils.darkThemeColor
import ir.dekot.eshterakyar.core.utils.lightThemeColor
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Status bar رو transparent کن
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        setContent {
            val viewModel: ThemeViewModel = koinViewModel()
            val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()

            // تشخیص تم سیستم
            val systemIsDark = isSystemInDarkTheme()

            // محاسبه تم نهایی بر اساس حالت انتخابی
            val isDark =
                    when (themeMode) {
                        ThemeMode.SYSTEM -> systemIsDark
                        ThemeMode.LIGHT -> false
                        ThemeMode.DARK -> true
                    }

            val themeColors = if (isDark) darkThemeColor else lightThemeColor

            // Status bar icons رو بر اساس تم تنظیم کن
            WindowCompat.getInsetsController(window, window.decorView).apply {
                isAppearanceLightStatusBars = !isDark
            }

            CompositionLocalProvider(LocalTheme provides themeColors) {
                EshterakYarTheme(darkTheme = isDark) { EshterakYarApp() }
            }
        }
    }
}
