package ir.dekot.eshterakyar.core.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// تعریف رنگ‌های خنثی (grayscale) برای تم لایت
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF616161), // طوسی متوسط (Gray 600) برای عناصر اصلی
    onPrimary = Color(0xFFFFFFFF), // سفید برای متن روی primary
    primaryContainer = Color(0xFFE0E0E0), // طوسی روشن برای کانتینرها
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

// تعریف رنگ‌های خنثی (grayscale) برای تم دارک
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBDBDBD), // طوسی روشن (Gray 300) برای عناصر اصلی در دارک
    onPrimary = Color(0xFF212121), // طوسی تیره برای متن روی primary
    primaryContainer = Color(0xFF424242), // طوسی متوسط برای کانتینرها
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
    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */

@Composable
fun EshterakYarTheme(
    darkTheme: Boolean ,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}