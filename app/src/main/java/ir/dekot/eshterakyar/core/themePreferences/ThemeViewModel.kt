package ir.dekot.eshterakyar.core.themePreferences

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ThemeViewModel(application: Application) : AndroidViewModel(application) {

    private val themePreferences = ThemePreferences(application.applicationContext)

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    init {
        // بارگذاری اولیه حالت از DataStore
        viewModelScope.launch {
            themePreferences.isDarkTheme.collectLatest { isDark ->
                _isDarkTheme.value = isDark
            }
        }
    }

    // تابع برای تغییر حالت و ذخیره آن
    fun toggleTheme() {
        viewModelScope.launch {
            val newValue = !_isDarkTheme.value
            _isDarkTheme.value = newValue
            themePreferences.setDarkTheme(newValue)
        }
    }
}
