package ir.dekot.eshterakyar.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.CategoryBreakdown
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.GetCategoryBreakdownUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.GetSubscriptionStatsUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.SubscriptionStats
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ویومدل صفحه گزارش‌ها
 *
 * بارگذاری آمار اشتراک‌ها و توزیع هزینه‌ها بر اساس دسته‌بندی
 */
class ReportsViewModel(
        private val getSubscriptionStatsUseCase: GetSubscriptionStatsUseCase,
        private val getCategoryBreakdownUseCase: GetCategoryBreakdownUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportsUiState())
    val uiState: StateFlow<ReportsUiState> = _uiState.asStateFlow()

    init {
        loadData()
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

                _uiState.value =
                        _uiState.value.copy(
                                stats = stats,
                                categoryBreakdown = categoryBreakdown,
                                isLoading = false
                        )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
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
 * @param isLoading وضعیت بارگذاری
 * @param error پیام خطا
 */
data class ReportsUiState(
        val stats: SubscriptionStats? = null,
        val categoryBreakdown: List<CategoryBreakdown> = emptyList(),
        val isLoading: Boolean = true,
        val error: String? = null
)
