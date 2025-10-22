package ir.dekot.eshterakyar.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.GetSubscriptionStatsUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.SubscriptionStats
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReportsViewModel(
    private val getSubscriptionStatsUseCase: GetSubscriptionStatsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportsUiState())
    val uiState: StateFlow<ReportsUiState> = _uiState.asStateFlow()

    init {
        loadStats()
    }

    private fun loadStats() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val stats = getSubscriptionStatsUseCase()
                _uiState.value = _uiState.value.copy(
                    stats = stats,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun refreshData() {
        loadStats()
    }
}

data class ReportsUiState(
    val stats: SubscriptionStats? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)