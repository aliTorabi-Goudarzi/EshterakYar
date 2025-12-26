package ir.dekot.eshterakyar.core.themePreferences

/**
 * حالت‌های مختلف تم اپلیکیشن
 *
 * @property persianName نام فارسی برای نمایش در UI
 */
enum class ThemeMode(val persianName: String) {
    /** پیروی از تنظیمات سیستم گوشی */
    SYSTEM("پیروی از سیستم"),

    /** همیشه تم روشن */
    LIGHT("روشن"),

    /** همیشه تم تاریک */
    DARK("تاریک")
}
