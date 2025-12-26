package ir.dekot.eshterakyar.core.themePreferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

// نام DataStore
private const val THEME_PREFERENCES_NAME = "theme_preferences"

// اکستنشن برای دسترسی به DataStore
val Context.themeDataStore: DataStore<Preferences> by preferencesDataStore(THEME_PREFERENCES_NAME)

/**
 * کلاس مدیریت ترجیحات تم اپلیکیشن
 *
 * این کلاس حالت تم (سیستم، روشن، تاریک) را با استفاده از DataStore ذخیره می‌کند
 *
 * @param context کانتکست اپلیکیشن
 */
class ThemePreferences(private val context: Context) {

    // کلید برای ذخیره حالت تم (ThemeMode)
    private val THEME_MODE_KEY = stringPreferencesKey("theme_mode")

    // کلید قدیمی برای سازگاری با نسخه‌های قبلی
    private val LEGACY_IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")

    /**
     * جریان (Flow) برای خواندن حالت فعلی تم
     *
     * اگر کلید جدید وجود نداشت، از کلید قدیمی مهاجرت می‌کند
     */
    val themeMode: Flow<ThemeMode> =
            context.themeDataStore.data.map { preferences ->
                // اول کلید جدید را بررسی کن
                val themeModeString = preferences[THEME_MODE_KEY]
                if (themeModeString != null) {
                    try {
                        ThemeMode.valueOf(themeModeString)
                    } catch (e: IllegalArgumentException) {
                        ThemeMode.SYSTEM
                    }
                } else {
                    // مهاجرت از کلید قدیمی
                    val legacyIsDark = preferences[LEGACY_IS_DARK_THEME] ?: false
                    if (legacyIsDark) ThemeMode.DARK else ThemeMode.SYSTEM
                }
            }

    /**
     * تابع برای ذخیره حالت تم جدید
     *
     * @param mode حالت تم جدید
     */
    suspend fun setThemeMode(mode: ThemeMode) {
        context.themeDataStore.edit { preferences ->
            preferences[THEME_MODE_KEY] = mode.name
            // حذف کلید قدیمی پس از مهاجرت
            preferences.remove(LEGACY_IS_DARK_THEME)
        }
    }

    /**
     * دریافت حالت تم به صورت همزمان (برای استفاده در ابتدای اپ)
     *
     * @return حالت تم فعلی
     */
    suspend fun getThemeModeSync(): ThemeMode {
        return themeMode.first()
    }
}
