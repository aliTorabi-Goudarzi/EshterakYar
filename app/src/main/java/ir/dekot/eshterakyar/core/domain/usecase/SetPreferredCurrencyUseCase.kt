package ir.dekot.eshterakyar.core.domain.usecase

import ir.dekot.eshterakyar.core.preferences.CurrencyPreferences
import ir.dekot.eshterakyar.core.utils.Currency

/**
 * ذخیره ارز پیش‌فرض کاربر
 */
class SetPreferredCurrencyUseCase(
    private val currencyPreferences: CurrencyPreferences
) {
    suspend operator fun invoke(currency: Currency) {
        currencyPreferences.setPreferredCurrency(currency)
    }
}
