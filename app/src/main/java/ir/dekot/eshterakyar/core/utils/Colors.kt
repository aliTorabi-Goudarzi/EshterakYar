package ir.dekot.eshterakyar.core.utils

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class CustomTheme(
    val buttonColor: Color,
    val backgroundColor: Color,
    val barColor: Color,
    val bottomBarUnselectedItemColor: Color,
    val bottomBarSelectedItemColor: Color,
    val primaryContainer: Color,
    val primary: Color,
    val onPrimary: Color,
    val onPrimaryContainer: Color,
    val secondary: Color,
    val onSecondary: Color,
    val secondaryContainer: Color,
    val tertiary: Color,
    val onTertiary: Color,
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color,
    val surfaceVariant: Color,
    val onSurfaceVariant: Color,
    val error: Color,
    val onError: Color,
    val onSecondaryContainer: Color,
)

val lightThemeColor = CustomTheme(
    buttonColor = Color.LightGray,
    backgroundColor = Color(0xFFf8f9fa),
    barColor = Color(0xFF343a40),
    bottomBarUnselectedItemColor = Color(0xFFf8f9fa),
    bottomBarSelectedItemColor = Color(0xFFf8f9fa),
            primaryContainer = Color(0xFFE0E0E0),
    primary = Color(0xFF616161), // طوسی متوسط (Gray 600) برای عناصر اصلی
    onPrimary = Color(0xFFFFFFFF), // سفید برای متن روی primary
    onPrimaryContainer = Color(0xFF212121), // طوسی تیره برای متن روی کانتینر
    secondary = Color(0xFF757575), // طوسی روشن‌تر (Gray 500) برای عناصر ثانویه
    onSecondary = Color(0xFFFFFFFF), // سفید برای متن روی secondary
    secondaryContainer = Color(0xFFF5F5F5), // طوسی خیلی روشن برای کانتینرها
    onSecondaryContainer = Color(0xFF424242), // طوسی متوسط برای متن
    tertiary = Color(0xFF9E9E9E), // طوسی روشن (Gray 400) برای عناصر سوم
    onTertiary = Color(0xFF000000), // سیاه برای متن روی tertiary

    background = Color(0xFFFFFFFF), // سفید برای پس‌زمینه اصلی
    onBackground = Color(0xFF212121), // طوسی تیره برای متن روی background

    surface = Color(0xFFFAFAFA), // طوسی خیلی روشن (Gray 50) برای سطوح مثل کارت‌ها
    onSurface = Color(0xFF212121), // طوسی تیره برای متن روی surface
    surfaceVariant = Color(0xFFE0E0E0), // variant برای تمایز
    onSurfaceVariant = Color(0xFF424242),

    error = Color(0xFFB71C1C), // قرمز خنثی برای خطاها (می‌تونید به طوسی تغییر بدید اگر نمی‌خواید هیچ رنگی)
    onError = Color(0xFFFFFFFF)
)

val darkThemeColor = CustomTheme(
    buttonColor = Color.DarkGray,
    backgroundColor = Color(0xFF0F1217),
    barColor = Color(0xFF343a40),
    bottomBarUnselectedItemColor = Color(0xFFf8f9fa),
    bottomBarSelectedItemColor = Color(0xFFf8f9fa),
    primaryContainer = Color(0xFF424242), // طوسی متوسط برای کانتینرها
    primary = Color(0xFFBDBDBD), // طوسی روشن (Gray 300) برای عناصر اصلی در دارک
    onPrimary = Color(0xFF212121), // طوسی تیره برای متن روی primary
    onPrimaryContainer = Color(0xFFE0E0E0), // طوسی روشن برای متن روی کانتینر
    secondary = Color(0xFF9E9E9E), // طوسی متوسط (Gray 400) برای عناصر ثانویه
    onSecondary = Color(0xFF212121), // طوسی تیره برای متن روی secondary
    secondaryContainer = Color(0xFF616161), // طوسی تیره‌تر برای کانتینرها
    onSecondaryContainer = Color(0xFFE0E0E0), // طوسی روشن برای متن
    tertiary = Color(0xFF757575), // طوسی تیره (Gray 500) برای عناصر سوم
    onTertiary = Color(0xFFFFFFFF), // سفید برای متن روی tertiary

    background = Color(0xFF212121), // طوسی خیلی تیره (Gray 900) برای پس‌زمینه اصلی
    onBackground = Color(0xFFE0E0E0), // طوسی روشن برای متن روی background

    surface = Color(0xFF424242), // طوسی تیره (Gray 800) برای سطوح مثل کارت‌ها
    onSurface = Color(0xFFE0E0E0), // طوسی روشن برای متن روی surface
    surfaceVariant = Color(0xFF616161), // variant برای تمایز
    onSurfaceVariant = Color(0xFFBDBDBD),

    error = Color(0xFFEF5350), // قرمز تیره برای خطاها (می‌تونید به طوسی تغییر بدید)
    onError = Color(0xFF212121)
)

val LocalTheme = staticCompositionLocalOf<CustomTheme> {
    error("No colors provided")
}