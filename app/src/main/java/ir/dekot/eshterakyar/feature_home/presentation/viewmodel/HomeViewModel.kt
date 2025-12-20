package ir.dekot.eshterakyar.feature_home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.GetActiveSubscriptionsUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.GetInactiveSubscriptionsUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.GetNearingRenewalSubscriptionsUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.GetSubscriptionStatsUseCase
import ir.dekot.eshterakyar.feature_home.domain.usecase.GetUserGreetingUseCase
import ir.dekot.eshterakyar.feature_home.presentation.mvi.HomeEffect
import ir.dekot.eshterakyar.feature_home.presentation.mvi.HomeIntent
import ir.dekot.eshterakyar.feature_home.presentation.mvi.HomeState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getActiveSubscriptionsUseCase: GetActiveSubscriptionsUseCase,
    private val getSubscriptionStatsUseCase: GetSubscriptionStatsUseCase,
    private val getInactiveSubscriptionsUseCase: GetInactiveSubscriptionsUseCase,
    private val getNearingRenewalSubscriptionsUseCase: GetNearingRenewalSubscriptionsUseCase,
    private val getUserGreetingUseCase: GetUserGreetingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()

    private val _effect = Channel<HomeEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadData()
    }

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.Refresh -> loadData()
            HomeIntent.OnAddSubscriptionClicked -> sendEffect(HomeEffect.NavigateToAddSubscription)
            is HomeIntent.OnSubscriptionClicked -> sendEffect(HomeEffect.NavigateToSubscriptionDetail(intent.id))
            is HomeIntent.OnEditSubscriptionClicked -> sendEffect(HomeEffect.NavigateToEditSubscription(intent.id))
            is HomeIntent.OnDeleteSubscription -> deleteSubscription(intent)
            is HomeIntent.OnToggleSubscriptionStatus -> toggleSubscriptionStatus(intent)
        }
    }

    private fun loadData() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        loadSubscriptions()
        loadStats()
        loadGreeting()
        loadAdditionalStats()
    }

    private fun sendEffect(effect: HomeEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    private fun loadSubscriptions() {
        viewModelScope.launch {
            try {
                getActiveSubscriptionsUseCase().collect { subscriptions ->
                    _uiState.value = _uiState.value.copy(
                        subscriptions = subscriptions,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    private fun loadStats() {
        viewModelScope.launch {
            try {
                val stats = getSubscriptionStatsUseCase()
                _uiState.value = _uiState.value.copy(
                    stats = stats
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message
                )
            }
        }
    }

    private fun loadGreeting() {
        viewModelScope.launch {
            try {
                val greeting = getUserGreetingUseCase.execute("علی ترابی گودرزی")
                _uiState.value = _uiState.value.copy(
                    greeting = greeting
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message
                )
            }
        }
    }

    private fun loadAdditionalStats() {
        viewModelScope.launch {
            try {
                val inactiveCount = getInactiveSubscriptionsUseCase()
                val nearingRenewalCount = getNearingRenewalSubscriptionsUseCase()
                _uiState.value = _uiState.value.copy(
                    inactiveCount = inactiveCount,
                    nearingRenewalCount = nearingRenewalCount
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message
                )
            }
        }
    }

    private fun deleteSubscription(intent: HomeIntent.OnDeleteSubscription) {
        // Placeholder for delete logic
        loadData()
    }

    private fun toggleSubscriptionStatus(intent: HomeIntent.OnToggleSubscriptionStatus) {
        // Placeholder for toggle logic
        loadData()
    }
}