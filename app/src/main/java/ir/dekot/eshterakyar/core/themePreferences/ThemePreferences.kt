package ir.dekot.eshterakyar.core.themePreferences


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// نام DataStore
private const val THEME_PREFERENCES_NAME = "theme_preferences"

// اکستنشن برای دسترسی به DataStore
//val Context.themeDataStore: DataStore<Preferences> by preferencesDataStore(THEME_PREFERENCES_NAME)
val Context.themeDataStore : DataStore<Preferences> by preferencesDataStore(THEME_PREFERENCES_NAME)

class ThemePreferences(private val context: Context) {

    // کلید برای ذخیره حالت دارک مود (پیش‌فرض: false یعنی لایت)
    private val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")

    // جریان (Flow) برای خواندن حالت فعلی
    val isDarkTheme: Flow<Boolean> = context.themeDataStore.data
        .map { preferences ->
            preferences[IS_DARK_THEME] ?: false
        }

    // تابع برای ذخیره حالت جدید
    suspend fun setDarkTheme(isDark: Boolean) {
        context.themeDataStore.edit { preferences ->
            preferences[IS_DARK_THEME] = isDark
        }
    }
}
