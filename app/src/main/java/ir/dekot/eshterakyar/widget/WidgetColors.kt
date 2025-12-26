package ir.dekot.eshterakyar.widget

import androidx.compose.ui.graphics.Color
import androidx.glance.color.ColorProvider
import androidx.glance.unit.ColorProvider

/** رنگ‌های ویجت برای تم روشن و تیره */
object WidgetColors {

    // رنگ پس‌زمینه ویجت
    val widgetBackground = ColorProvider(day = Color(0xFFF5F5F5), night = Color(0xFF1E1E1E))

    // رنگ پس‌زمینه هدر
    val headerBackground = ColorProvider(day = Color(0xFF3498DB), night = Color(0xFF2980B9))

    // رنگ متن اصلی
    val textPrimary = ColorProvider(day = Color(0xFF212121), night = Color(0xFFE0E0E0))

    // رنگ متن ثانویه
    val textSecondary = ColorProvider(day = Color(0xFF757575), night = Color(0xFF9E9E9E))

    // رنگ متن روی هدر
    val textOnHeader = ColorProvider(day = Color.White, night = Color.White)

    // رنگ آیتم‌ها
    val itemBackground = ColorProvider(day = Color.White, night = Color(0xFF2D2D2D))

    // رنگ حاشیه
    val divider = ColorProvider(day = Color(0xFFE0E0E0), night = Color(0xFF424242))
}
