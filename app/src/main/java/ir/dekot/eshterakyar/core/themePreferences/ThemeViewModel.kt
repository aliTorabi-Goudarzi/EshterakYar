package ir.dekot.eshterakyar.core.themePreferences

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * ویومدل مدیریت تم اپلیکیشن
 *
 * این ویومدل حالت تم را از DataStore خوانده و امکان تغییر آن را فراهم می‌کند
 *
 * @param application اپلیکیشن اندروید
 */
class ThemeViewModel(application: Application) : AndroidViewModel(application) {

    private val themePreferences = ThemePreferences(application.applicationContext)

    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)
    /** حالت فعلی تم */
    val themeMode: StateFlow<ThemeMode> = _themeMode

    init {
        // بارگذاری اولیه حالت از DataStore
        viewModelScope.launch {
            themePreferences.themeMode.collectLatest { mode -> _themeMode.value = mode }
        }
    }

    /**
     * تغییر حالت تم
     *
     * @param mode حالت جدید تم
     */
    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            _themeMode.value = mode
            themePreferences.setThemeMode(mode)
        }
    }
}
