package ir.dekot.eshterakyar.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.dekot.eshterakyar.core.domain.usecase.GetPreferredCurrencyUseCase
import ir.dekot.eshterakyar.core.domain.usecase.SetPreferredCurrencyUseCase
import ir.dekot.eshterakyar.core.utils.Currency
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ویومدل تنظیمات برنامه
 * 
 * مدیریت تنظیمات ارز پیش‌فرض
 */
class SettingsViewModel(
    private val getPreferredCurrencyUseCase: GetPreferredCurrencyUseCase,
    private val setPreferredCurrencyUseCase: SetPreferredCurrencyUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadPreferredCurrency()
    }

    /**
     * بارگذاری ارز پیش‌فرض از دیتااستور
     */
    private fun loadPreferredCurrency() {
        getPreferredCurrencyUseCase()
            .onEach { currency ->
                _uiState.update { it.copy(preferredCurrency = currency) }
            }
            .launchIn(viewModelScope)
    }

    /**
     * تغییر ارز پیش‌فرض
     * @param currency ارز جدید
     */
    fun setPreferredCurrency(currency: Currency) {
        viewModelScope.launch {
            setPreferredCurrencyUseCase(currency)
        }
    }
}

/**
 * وضعیت UI صفحه تنظیمات
 */
data class SettingsUiState(
    val preferredCurrency: Currency = Currency.IRT
)
