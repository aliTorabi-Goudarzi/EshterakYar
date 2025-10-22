package ir.dekot.eshterakyar.feature_addSubscription.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.BillingCycle
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.Subscription
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.SubscriptionCategory
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.InsertSubscriptionUseCase
import ir.dekot.eshterakyar.feature_addSubscription.presentation.state.AddSubscriptionUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class AddSubscriptionViewModel(
    private val insertSubscriptionUseCase: InsertSubscriptionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddSubscriptionUiState())
    val uiState: StateFlow<AddSubscriptionUiState> = _uiState.asStateFlow()

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

    fun updateCategory(category: SubscriptionCategory) {
        _uiState.value = _uiState.value.copy(category = category)
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

        val newSubscription = Subscription(
            name = _uiState.value.name,
            price = _uiState.value.price.toDouble(),
            currency = _uiState.value.currency,
            billingCycle = _uiState.value.billingCycle,
            startDate = Date(), // تاریخ شروع امروز
            nextRenewalDate = _uiState.value.nextRenewalDate,
            isActive = _uiState.value.isActive,
            category = _uiState.value.category
        )

        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isSaving = true)
                insertSubscriptionUseCase(newSubscription)
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    saveSuccess = true
                )
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

    fun resetSaveSuccess() {
        _uiState.value = _uiState.value.copy(saveSuccess = false)
    }
}