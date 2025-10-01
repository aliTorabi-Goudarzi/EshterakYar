package ir.dekot.eshterakyar.core.utils

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class CustomTheme(
    val buttonColor: Color,
    val backgroundColor: Color,
    val barColor: Color,
    val bottomBarUnselectedItemColor: Color,
    val bottomBarSelectedItemColor: Color,
)

val lightThemeColor = CustomTheme(
    buttonColor = Color.LightGray,
    backgroundColor = Color(0xFFf8f9fa),
    barColor = Color(0xFF343a40),
    bottomBarUnselectedItemColor = Color(0xFFf8f9fa),
    bottomBarSelectedItemColor = Color(0xFFf8f9fa)
)

val darkThemeColor = CustomTheme(
    buttonColor = Color.DarkGray,
    backgroundColor = Color(0xFF0F1217),
    barColor = Color(0xFF343a40),
    bottomBarUnselectedItemColor = Color(0xFFf8f9fa),
    bottomBarSelectedItemColor = Color(0xFFf8f9fa)
)

val LocalTheme = staticCompositionLocalOf<CustomTheme> {
    error("No colors provided")
}