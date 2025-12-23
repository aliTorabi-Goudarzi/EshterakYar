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

import ir.dekot.eshterakyar.feature_addSubscription.presentation.intent.AddSubscriptionIntent

class AddSubscriptionViewModel(
    private val insertSubscriptionUseCase: InsertSubscriptionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddSubscriptionUiState())
    val uiState: StateFlow<AddSubscriptionUiState> = _uiState.asStateFlow()

    fun onIntent(intent: AddSubscriptionIntent) {
        when (intent) {
            is AddSubscriptionIntent.OnNameChanged -> updateName(intent.name)
            is AddSubscriptionIntent.OnPriceChanged -> updatePrice(intent.price)
            is AddSubscriptionIntent.OnCurrencyChanged -> updateCurrency(intent.currency)
            is AddSubscriptionIntent.OnBillingCycleChanged -> updateBillingCycle(intent.cycle)
            is AddSubscriptionIntent.OnNextRenewalDateChanged -> updateNextRenewalDate(intent.date)
            is AddSubscriptionIntent.OnActiveStatusChanged -> updateIsActive(intent.isActive)
            is AddSubscriptionIntent.OnCategoryChanged -> updateCategory(intent.category)
            AddSubscriptionIntent.OnNextStep -> onNextStep()
            AddSubscriptionIntent.OnPreviousStep -> onPreviousStep()
            AddSubscriptionIntent.OnSaveClicked -> saveSubscription()
            AddSubscriptionIntent.OnErrorDismissed -> clearError()
            AddSubscriptionIntent.OnSuccessDismissed -> resetSaveSuccess()
        }
    }

    private fun onNextStep() {
        val currentStep = _uiState.value.currentStep
        if (validateStep(currentStep)) {
            _uiState.value = _uiState.value.copy(currentStep = currentStep + 1)
        }
    }

    private fun onPreviousStep() {
        val currentStep = _uiState.value.currentStep
        if (currentStep > 1) {
            _uiState.value = _uiState.value.copy(currentStep = currentStep - 1)
        }
    }

    private fun validateStep(step: Int): Boolean {
        return when (step) {
            1 -> { // Basic Info
                val nameValid = _uiState.value.name.isNotBlank()
                _uiState.value = _uiState.value.copy(
                    nameError = if (!nameValid) "نام اشتراک نمی‌تواند خالی باشد" else null
                )
                nameValid
            }
            2 -> { // Payment Details
                val priceValid = _uiState.value.price.isNotBlank() && 
                                (_uiState.value.price.toDoubleOrNull() ?: 0.0) > 0
                _uiState.value = _uiState.value.copy(
                    priceError = if (!priceValid) "قیمت باید یک عدد مثبت باشد" else null
                )
                priceValid
            }
            else -> true
        }
    }

    private fun updateName(name: String) {
        _uiState.value = _uiState.value.copy(
            name = name,
            nameError = if (name.isBlank()) "نام اشتراک نمی‌تواند خالی باشد" else null
        )
    }

    private fun updatePrice(price: String) {
        _uiState.value = _uiState.value.copy(
            price = price,
            priceError = if (price.isBlank() || price.toDoubleOrNull() == null || price.toDouble() <= 0) 
                "قیمت باید یک عدد مثبت باشد" 
            else null
        )
    }

    private fun updateCurrency(currency: String) {
        _uiState.value = _uiState.value.copy(currency = currency)
    }

    private fun updateBillingCycle(billingCycle: BillingCycle) {
        _uiState.value = _uiState.value.copy(billingCycle = billingCycle)
    }

    private fun updateNextRenewalDate(date: Date) {
        _uiState.value = _uiState.value.copy(nextRenewalDate = date)
    }

    private fun updateIsActive(isActive: Boolean) {
        _uiState.value = _uiState.value.copy(isActive = isActive)
    }

    private fun updateCategory(category: SubscriptionCategory) {
        _uiState.value = _uiState.value.copy(category = category)
    }

    private fun validateAndSave(): Boolean {
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

    private fun saveSubscription() {
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

    private fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    private fun resetSaveSuccess() {
        _uiState.value = AddSubscriptionUiState()
    }
}