package ir.dekot.eshterakyar.feature_home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.dekot.eshterakyar.core.navigation.RootNavigator
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.BillingCycle
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.Subscription
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.GetSubscriptionByIdUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.UpdateSubscriptionUseCase
import ir.dekot.eshterakyar.feature_home.presentation.state.EditSubscriptionUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class EditSubscriptionViewModel(
    private val getSubscriptionByIdUseCase: GetSubscriptionByIdUseCase,
    private val updateSubscriptionUseCase: UpdateSubscriptionUseCase,
    private val rootNavigator: RootNavigator
) : ViewModel() {

    fun goBack(){
        rootNavigator.goBack()
    }
    private val _uiState = MutableStateFlow(EditSubscriptionUiState())
    val uiState: StateFlow<EditSubscriptionUiState> = _uiState.asStateFlow()

    fun loadSubscription(subscriptionId: Long) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val subscription = getSubscriptionByIdUseCase(subscriptionId)
                subscription?.let {
                    _uiState.value = _uiState.value.copy(
                        subscription = it,
                        isLoading = false,
                        name = it.name,
                        price = it.price.toString(),
                        currency = it.currency,
                        billingCycle = it.billingCycle,
                        nextRenewalDate = it.nextRenewalDate,
                        isActive = it.isActive
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

    fun updateName(name: String) {
        _uiState.value = _uiState.value.copy(
            name = name,
            nameError = if (name.isBlank()) "نام اشتراک نمی‌تواند خالی باشد" else null
        )
    }

    fun updatePrice(price: String) {
        _uiState.value = _uiState.value.copy(
            price = price,
            priceError = if (price.isBlank() || price.toDoubleOrNull() == null || price.toDouble() <= 0) 
                "قیمت باید یک عدد مثبت باشد" 
            else null
        )
    }

    fun updateCurrency(currency: String) {
        _uiState.value = _uiState.value.copy(currency = currency)
    }

    fun updateBillingCycle(billingCycle: BillingCycle) {
        _uiState.value = _uiState.value.copy(billingCycle = billingCycle)
    }

    fun updateNextRenewalDate(date: Date) {
        _uiState.value = _uiState.value.copy(nextRenewalDate = date)
    }

    fun updateIsActive(isActive: Boolean) {
        _uiState.value = _uiState.value.copy(isActive = isActive)
    }

    fun validateAndSave(): Boolean {
        val nameError = if (_uiState.value.name.isBlank()) "نام اشتراک نمی‌تواند خالی باشد" else null
        val priceError = if (_uiState.value.price.isBlank() || 
                              _uiState.value.price.toDoubleOrNull() == null || 
                              _uiState.value.price.toDouble() <= 0) 
            "قیمت باید یک عدد مثبت باشد" 
        else null

        _uiState.value = _uiState.value.copy(
            nameError = nameError,
            priceError = priceError
        )

        return nameError == null && priceError == null
    }

    fun saveSubscription() {
        if (!validateAndSave()) return

        val originalSubscription = _uiState.value.subscription ?: return
        
        val updatedSubscription = originalSubscription.copy(
            name = _uiState.value.name,
            price = _uiState.value.price.toDouble(),
            currency = _uiState.value.currency,
            billingCycle = _uiState.value.billingCycle,
            nextRenewalDate = _uiState.value.nextRenewalDate,
            isActive = _uiState.value.isActive
        )

        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isSaving = true)
                updateSubscriptionUseCase(updatedSubscription)
                _uiState.value = _uiState.value.copy(isSaving = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    error = e.message
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}