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
import ir.dekot.eshterakyar.feature_payment.domain.usecase.RecordPaymentUseCase
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
        private val getUserGreetingUseCase: GetUserGreetingUseCase,
        private val recordPaymentUseCase: RecordPaymentUseCase
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
            is HomeIntent.OnSubscriptionClicked ->
                    sendEffect(HomeEffect.NavigateToSubscriptionDetail(intent.id))
            is HomeIntent.OnEditSubscriptionClicked ->
                    sendEffect(HomeEffect.NavigateToEditSubscription(intent.id))
            is HomeIntent.OnDeleteSubscription -> deleteSubscription(intent)
            is HomeIntent.OnToggleSubscriptionStatus -> toggleSubscriptionStatus(intent)
            is HomeIntent.OnSearchQueryChanged -> filterSubscriptions(intent.query)
            is HomeIntent.OnSortOptionChanged -> changeSortOption(intent.option)
            is HomeIntent.OnPaymentConfirmed -> recordPayment(intent.subscription)
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
        viewModelScope.launch { _effect.send(effect) }
    }

    private fun loadSubscriptions() {
        viewModelScope.launch {
            try {
                getActiveSubscriptionsUseCase().collect { subscriptions ->
                    val currentQuery = _uiState.value.searchQuery
                    val currentSort = _uiState.value.selectedSortOption
                    _uiState.value =
                            _uiState.value.copy(
                                    subscriptions = subscriptions,
                                    filteredSubscriptions =
                                            applyFilterAndSort(
                                                    subscriptions,
                                                    currentQuery,
                                                    currentSort
                                            ),
                                    isLoading = false
                            )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    /** فیلتر کردن اشتراک‌ها بر اساس متن جستجو */
    private fun filterSubscriptions(query: String) {
        val subscriptions = _uiState.value.subscriptions
        val sortOption = _uiState.value.selectedSortOption
        _uiState.value =
                _uiState.value.copy(
                        searchQuery = query,
                        filteredSubscriptions = applyFilterAndSort(subscriptions, query, sortOption)
                )
    }

    /** تغییر نوع مرتب‌سازی */
    private fun changeSortOption(
            option: ir.dekot.eshterakyar.feature_home.presentation.mvi.SortOption
    ) {
        val subscriptions = _uiState.value.subscriptions
        val query = _uiState.value.searchQuery
        _uiState.value =
                _uiState.value.copy(
                        selectedSortOption = option,
                        filteredSubscriptions = applyFilterAndSort(subscriptions, query, option)
                )
    }

    /** اعمال فیلتر و مرتب‌سازی روی لیست اشتراک‌ها */
    private fun applyFilterAndSort(
            subscriptions:
                    List<ir.dekot.eshterakyar.feature_addSubscription.domain.model.Subscription>,
            query: String,
            sortOption: ir.dekot.eshterakyar.feature_home.presentation.mvi.SortOption
    ): List<ir.dekot.eshterakyar.feature_addSubscription.domain.model.Subscription> {
        val filtered =
                if (query.isBlank()) {
                    subscriptions
                } else {
                    subscriptions.filter {
                        it.name.contains(query, ignoreCase = true) ||
                                it.description.contains(query, ignoreCase = true)
                    }
                }

        return when (sortOption) {
            ir.dekot.eshterakyar.feature_home.presentation.mvi.SortOption.RENEWAL_SOON ->
                    filtered.sortedBy { it.nextRenewalDate }
            ir.dekot.eshterakyar.feature_home.presentation.mvi.SortOption.RENEWAL_FAR ->
                    filtered.sortedByDescending { it.nextRenewalDate }
            ir.dekot.eshterakyar.feature_home.presentation.mvi.SortOption.PRICE_HIGH ->
                    filtered.sortedByDescending { it.price }
            ir.dekot.eshterakyar.feature_home.presentation.mvi.SortOption.PRICE_LOW ->
                    filtered.sortedBy { it.price }
            ir.dekot.eshterakyar.feature_home.presentation.mvi.SortOption.NAME_ASC ->
                    filtered.sortedBy { it.name }
            ir.dekot.eshterakyar.feature_home.presentation.mvi.SortOption.NAME_DESC ->
                    filtered.sortedByDescending { it.name }
        }
    }

    private fun loadStats() {
        viewModelScope.launch {
            try {
                val stats = getSubscriptionStatsUseCase()
                _uiState.value = _uiState.value.copy(stats = stats)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    private fun loadGreeting() {
        viewModelScope.launch {
            try {
                val greeting = getUserGreetingUseCase.execute("علی ترابی گودرزی")
                _uiState.value = _uiState.value.copy(greeting = greeting)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    private fun loadAdditionalStats() {
        viewModelScope.launch {
            try {
                val inactiveCount = getInactiveSubscriptionsUseCase()
                val nearingRenewalCount = getNearingRenewalSubscriptionsUseCase()
                _uiState.value =
                        _uiState.value.copy(
                                inactiveCount = inactiveCount,
                                nearingRenewalCount = nearingRenewalCount
                        )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
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

    /** ثبت پرداخت دستی برای یک اشتراک */
    private fun recordPayment(
            subscription: ir.dekot.eshterakyar.feature_addSubscription.domain.model.Subscription
    ) {
        viewModelScope.launch {
            try {
                recordPaymentUseCase(
                        subscriptionId = subscription.id,
                        amount = subscription.price,
                        note = "پرداخت دستی"
                )
                sendEffect(HomeEffect.ShowMessage("پرداخت با موفقیت ثبت شد"))
            } catch (e: Exception) {
                sendEffect(HomeEffect.ShowMessage("خطا در ثبت پرداخت: ${e.message}"))
            }
        }
    }
}
