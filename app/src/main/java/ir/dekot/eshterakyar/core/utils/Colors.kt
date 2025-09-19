package ir.dekot.eshterakyar.core.utils

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class CustomTheme(
    val buttonColor: Color
)

val lightThemeColor = CustomTheme(
    buttonColor = Color.LightGray
)

val darkThemeColor = CustomTheme(
    buttonColor = Color.DarkGray
)

val LocalTheme = staticCompositionLocalOf<CustomTheme> {
    error("No colors provided")
}