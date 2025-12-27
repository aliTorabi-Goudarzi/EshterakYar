package ir.dekot.eshterakyar.core.domain.usecase

import ir.dekot.eshterakyar.core.preferences.BudgetPreferences

/**
 * یوزکیس تنظیم بودجه ماهانه جدید
 *
 * این کلاس بودجه ماهانه جدید را در DataStore ذخیره می‌کند
 *
 * @param budgetPreferences کلاس مدیریت ترجیحات بودجه
 */
class SetBudgetUseCase(private val budgetPreferences: BudgetPreferences) {

    /**
     * تنظیم بودجه ماهانه جدید
     *
     * @param budget مقدار بودجه جدید (به تومان)
     */
    suspend operator fun invoke(budget: Double) {
        require(budget >= 0) { "بودجه نمی‌تواند منفی باشد" }
        budgetPreferences.setMonthlyBudget(budget)
    }
}
