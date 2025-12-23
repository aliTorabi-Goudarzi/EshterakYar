package ir.dekot.eshterakyar.core.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import ir.dekot.eshterakyar.core.themePreferences.themeDataStore
import ir.dekot.eshterakyar.core.utils.Currency
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * مدیریت تنظیمات ارز در دیتااستور
 * 
 * ذخیره و بازیابی ارز پیش‌فرض کاربر
 */
class CurrencyPreferences(private val context: Context) {

    // کلید برای ذخیره کد ارز پیش‌فرض
    private val preferredCurrencyKey = stringPreferencesKey("preferred_currency")

    /**
     * جریان (Flow) برای خواندن ارز پیش‌فرض
     * پیش‌فرض: تومان (IRT)
     */
    val preferredCurrency: Flow<Currency> = context.themeDataStore.data
        .map { preferences ->
            val code = preferences[preferredCurrencyKey] ?: Currency.IRT.code
            Currency.fromCode(code)
        }

    /**
     * ذخیره ارز پیش‌فرض جدید
     * @param currency ارز مورد نظر
     */
    suspend fun setPreferredCurrency(currency: Currency) {
        context.themeDataStore.edit { preferences ->
            preferences[preferredCurrencyKey] = currency.code
        }
    }
}
