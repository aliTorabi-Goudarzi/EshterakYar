package ir.dekot.eshterakyar.feature_home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.Subscription
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.DeleteSubscriptionUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.GetSubscriptionByIdUseCase
import ir.dekot.eshterakyar.feature_home.presentation.state.SubscriptionDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SubscriptionDetailViewModel(
    private val getSubscriptionByIdUseCase: GetSubscriptionByIdUseCase,
    private val deleteSubscriptionUseCase: DeleteSubscriptionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SubscriptionDetailUiState())
    val uiState: StateFlow<SubscriptionDetailUiState> = _uiState.asStateFlow()

    fun loadSubscription(subscriptionId: Long) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val subscription = getSubscriptionByIdUseCase(subscriptionId)
                _uiState.value = _uiState.value.copy(
                    subscription = subscription,
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

    fun deleteSubscription(subscription: Subscription) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isDeleting = true)
                deleteSubscriptionUseCase(subscription)
                _uiState.value = _uiState.value.copy(isDeleting = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isDeleting = false,
                    error = e.message
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}