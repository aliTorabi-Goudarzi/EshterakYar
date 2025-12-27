package ir.dekot.eshterakyar.core.domain.usecase

import ir.dekot.eshterakyar.core.preferences.BudgetPreferences
import kotlinx.coroutines.flow.Flow

/**
 * یوزکیس دریافت بودجه ماهانه کاربر
 *
 * این کلاس جریان بودجه ماهانه را از DataStore برمی‌گرداند
 *
 * @param budgetPreferences کلاس مدیریت ترجیحات بودجه
 */
class GetBudgetUseCase(private val budgetPreferences: BudgetPreferences) {

    /**
     * دریافت جریان بودجه ماهانه
     *
     * @return Flow مقدار بودجه ماهانه
     */
    operator fun invoke(): Flow<Double> = budgetPreferences.monthlyBudget
}
