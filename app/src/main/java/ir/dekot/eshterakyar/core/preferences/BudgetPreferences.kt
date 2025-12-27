package ir.dekot.eshterakyar.core.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/** نام DataStore برای ذخیره تنظیمات بودجه */
private const val BUDGET_PREFERENCES_NAME = "budget_preferences"

/** اکستنشن برای دسترسی به DataStore بودجه */
val Context.budgetDataStore: DataStore<Preferences> by preferencesDataStore(BUDGET_PREFERENCES_NAME)

/**
 * کلاس مدیریت ترجیحات بودجه ماهانه
 *
 * این کلاس بودجه ماهانه کاربر را با استفاده از DataStore ذخیره و بازیابی می‌کند
 *
 * @param context کانتکست اپلیکیشن
 */
class BudgetPreferences(private val context: Context) {

    /** کلید برای ذخیره بودجه ماهانه */
    private val MONTHLY_BUDGET_KEY = doublePreferencesKey("monthly_budget")

    companion object {
        /** بودجه پیش‌فرض: ۵۰۰,۰۰۰ تومان */
        const val DEFAULT_BUDGET = 500000.0
    }

    /**
     * جریان (Flow) برای خواندن بودجه ماهانه
     *
     * اگر بودجه‌ای ذخیره نشده باشد، مقدار پیش‌فرض برگردانده می‌شود
     */
    val monthlyBudget: Flow<Double> =
            context.budgetDataStore.data.map { preferences ->
                preferences[MONTHLY_BUDGET_KEY] ?: DEFAULT_BUDGET
            }

    /**
     * تنظیم بودجه ماهانه جدید
     *
     * @param budget مقدار بودجه جدید (به تومان)
     */
    suspend fun setMonthlyBudget(budget: Double) {
        context.budgetDataStore.edit { preferences -> preferences[MONTHLY_BUDGET_KEY] = budget }
    }

    /**
     * دریافت همزمان بودجه ماهانه (برای استفاده در ابتدای اپ)
     *
     * @return بودجه ماهانه فعلی
     */
    suspend fun getMonthlyBudgetSync(): Double {
        return monthlyBudget.first()
    }
}
