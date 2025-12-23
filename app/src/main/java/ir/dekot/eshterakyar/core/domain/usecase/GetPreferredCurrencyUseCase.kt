package ir.dekot.eshterakyar.core.domain.usecase

import ir.dekot.eshterakyar.core.preferences.CurrencyPreferences
import ir.dekot.eshterakyar.core.utils.Currency
import kotlinx.coroutines.flow.Flow

/**
 * دریافت ارز پیش‌فرض کاربر
 */
class GetPreferredCurrencyUseCase(
    private val currencyPreferences: CurrencyPreferences
) {
    operator fun invoke(): Flow<Currency> {
        return currencyPreferences.preferredCurrency
    }
}
