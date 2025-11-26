package ir.dekot.eshterakyar.feature_home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.dekot.eshterakyar.core.navigation.BottomBarItem
import ir.dekot.eshterakyar.core.navigation.NestedNavigator
import ir.dekot.eshterakyar.core.navigation.RootNavigator
import ir.dekot.eshterakyar.core.navigation.Screens
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.Subscription
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.GetActiveSubscriptionsUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.GetInactiveSubscriptionsUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.GetNearingRenewalSubscriptionsUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.GetSubscriptionStatsUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.SubscriptionStats
import ir.dekot.eshterakyar.feature_home.domain.model.UserGreeting
import ir.dekot.eshterakyar.feature_home.domain.usecase.GetUserGreetingUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getActiveSubscriptionsUseCase: GetActiveSubscriptionsUseCase,
    private val getSubscriptionStatsUseCase: GetSubscriptionStatsUseCase,
    private val getInactiveSubscriptionsUseCase: GetInactiveSubscriptionsUseCase,
    private val getNearingRenewalSubscriptionsUseCase: GetNearingRenewalSubscriptionsUseCase,
    private val getUserGreetingUseCase: GetUserGreetingUseCase,
    private val nestedNavigator: NestedNavigator,
    private val rootNavigator: RootNavigator
) : ViewModel() {

    fun navigateToAddSubscription(){
        nestedNavigator.navigateTo(destination = BottomBarItem.AddSubscription)
    }

    fun navigateToEditSubscription(id : Long){
        rootNavigator.navigateTo(destination = Screens.EditSubscription(subscriptionId = id))
    }

    fun navigateToSubscriptionDetail(id : Long){
        rootNavigator.navigateTo(destination = Screens.SubscriptionDetail(subscriptionId = id))
    }

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadSubscriptions()
        loadStats()
        loadGreeting()
        loadAdditionalStats()
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

    fun refreshData() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        loadSubscriptions()
        loadStats()
        loadAdditionalStats()
    }

    fun deleteSubscription(subscription: Subscription) {
        // In a real implementation, this would call a delete use case
        // For now, we'll just refresh the data
        viewModelScope.launch {
            try {
                // TODO: Implement deleteSubscriptionUseCase
                refreshData()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message
                )
            }
        }
    }

    fun toggleSubscriptionStatus(subscription: Subscription) {
        // In a real implementation, this would call a toggle status use case
        // For now, we'll just refresh the data
        viewModelScope.launch {
            try {
                // TODO: Implement toggleSubscriptionStatusUseCase
                refreshData()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message
                )
            }
        }
    }
}

data class HomeUiState(
    val subscriptions: List<Subscription> = emptyList(),
    val stats: SubscriptionStats? = null,
    val greeting: UserGreeting? = null,
    val inactiveCount: Int = 0,
    val nearingRenewalCount: Int = 0,
    val isLoading: Boolean = true,
    val error: String? = null
)
