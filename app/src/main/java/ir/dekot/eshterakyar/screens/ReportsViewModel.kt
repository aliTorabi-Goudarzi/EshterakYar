package ir.dekot.eshterakyar.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.dekot.eshterakyar.core.domain.usecase.GetBudgetUseCase
import ir.dekot.eshterakyar.core.domain.usecase.SetBudgetUseCase
import ir.dekot.eshterakyar.core.preferences.BudgetPreferences
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.CategoryBreakdown
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.GetCategoryBreakdownUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.GetSubscriptionStatsUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.SubscriptionStats
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ویومدل صفحه گزارش‌ها
 *
 * بارگذاری آمار اشتراک‌ها، توزیع هزینه‌ها بر اساس دسته‌بندی و مدیریت بودجه
 */
class ReportsViewModel(
        private val getSubscriptionStatsUseCase: GetSubscriptionStatsUseCase,
        private val getCategoryBreakdownUseCase: GetCategoryBreakdownUseCase,
        private val getBudgetUseCase: GetBudgetUseCase,
        private val setBudgetUseCase: SetBudgetUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportsUiState())
    val uiState: StateFlow<ReportsUiState> = _uiState.asStateFlow()

    init {
        loadData()
        observeBudget()
    }

    /** بارگذاری همه داده‌های گزارش */
    private fun loadData() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                // بارگذاری آمار
                val stats = getSubscriptionStatsUseCase()

                // بارگذاری توزیع دسته‌بندی‌ها
                val categoryBreakdown = getCategoryBreakdownUseCase()

                // بارگذاری بودجه
                val budget = getBudgetUseCase().first()

                _uiState.value =
                        _uiState.value.copy(
                                stats = stats,
                                categoryBreakdown = categoryBreakdown,
                                budget = budget,
                                isLoading = false
                        )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    /** مشاهده تغییرات بودجه */
    private fun observeBudget() {
        viewModelScope.launch {
            getBudgetUseCase().collect { budget ->
                _uiState.value = _uiState.value.copy(budget = budget)
            }
        }
    }

    /** به‌روزرسانی بودجه ماهانه */
    fun updateBudget(newBudget: Double) {
        viewModelScope.launch {
            try {
                setBudgetUseCase(newBudget)
                // بودجه به صورت خودکار از طریق Flow به‌روز می‌شود
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    /** بارگذاری مجدد داده‌ها */
    fun refreshData() {
        loadData()
    }
}

/**
 * وضعیت UI صفحه گزارش‌ها
 *
 * @param stats آمار کلی اشتراک‌ها
 * @param categoryBreakdown توزیع هزینه بر اساس دسته‌بندی
 * @param budget بودجه ماهانه کاربر
 * @param isLoading وضعیت بارگذاری
 * @param error پیام خطا
 */
data class ReportsUiState(
        val stats: SubscriptionStats? = null,
        val categoryBreakdown: List<CategoryBreakdown> = emptyList(),
        val budget: Double = BudgetPreferences.DEFAULT_BUDGET,
        val isLoading: Boolean = true,
        val error: String? = null
)
