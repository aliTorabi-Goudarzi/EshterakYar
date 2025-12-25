package ir.dekot.eshterakyar.feature_addSubscription.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.BillingCycle
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.ServicePreset
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.Subscription
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.SubscriptionCategory
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.DeleteSubscriptionUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.GetServicePresetsUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.GetSubscriptionsSortedByCreationUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.InsertSubscriptionUseCase
import ir.dekot.eshterakyar.feature_addSubscription.presentation.intent.AddSubscriptionIntent
import ir.dekot.eshterakyar.feature_addSubscription.presentation.state.AddSubscriptionUiState
import java.util.Date
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddSubscriptionViewModel(
        private val insertSubscriptionUseCase: InsertSubscriptionUseCase,
        private val getSubscriptionsSortedByCreationUseCase:
                GetSubscriptionsSortedByCreationUseCase,
        private val deleteSubscriptionUseCase: DeleteSubscriptionUseCase,
        private val getServicePresetsUseCase: GetServicePresetsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddSubscriptionUiState())
    val uiState: StateFlow<AddSubscriptionUiState> = _uiState.asStateFlow()

    init {
        loadSubscriptions()
        loadPresets()
    }

    private fun loadPresets() {
        val presets = getServicePresetsUseCase()
        _uiState.value = _uiState.value.copy(servicePresets = presets)
    }

    private fun loadSubscriptions() {
        viewModelScope.launch {
            getSubscriptionsSortedByCreationUseCase().collect { subscriptions ->
                _uiState.value = _uiState.value.copy(subscriptions = subscriptions)
            }
        }
    }

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
            is AddSubscriptionIntent.OnPresetSelected -> onPresetSelected(intent.preset)
            AddSubscriptionIntent.OnPresetCleared -> onPresetCleared()
            is AddSubscriptionIntent.OnSubscriptionClicked -> {
                _uiState.value =
                        _uiState.value.copy(
                                selectedSubscription = intent.subscription,
                                isBottomSheetOpen = true
                        )
            }
            AddSubscriptionIntent.OnBottomSheetDismissed -> {
                _uiState.value =
                        _uiState.value.copy(isBottomSheetOpen = false, selectedSubscription = null)
            }
            AddSubscriptionIntent.OnDeleteClicked -> {
                _uiState.value = _uiState.value.copy(isDeleteDialogVisible = true)
            }
            AddSubscriptionIntent.OnDeleteConfirmed -> deleteSelectedSubscription()
            AddSubscriptionIntent.OnDeleteCancelled -> {
                _uiState.value = _uiState.value.copy(isDeleteDialogVisible = false)
            }
            is AddSubscriptionIntent.OnEditClicked -> {
                // Navigation is handled by UI (SideEffect), but we close the sheet
                _uiState.value =
                        _uiState.value.copy(isBottomSheetOpen = false, selectedSubscription = null)
            }
        }
    }

    private fun deleteSelectedSubscription() {
        val subToDelete = _uiState.value.selectedSubscription ?: return

        viewModelScope.launch {
            try {
                deleteSubscriptionUseCase(subToDelete)
                _uiState.value =
                        _uiState.value.copy(
                                isDeleteDialogVisible = false,
                                isBottomSheetOpen = false,
                                selectedSubscription = null
                        )
            } catch (e: Exception) {
                _uiState.value =
                        _uiState.value.copy(
                                error = "خطا در حذف اشتراک: ${e.message}",
                                isDeleteDialogVisible = false
                        )
            }
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
                _uiState.value =
                        _uiState.value.copy(
                                nameError =
                                        if (!nameValid) "نام اشتراک نمی‌تواند خالی باشد" else null
                        )
                nameValid
            }
            2 -> { // Payment Details
                val priceValid =
                        _uiState.value.price.isNotBlank() &&
                                (_uiState.value.price.toDoubleOrNull() ?: 0.0) > 0
                _uiState.value =
                        _uiState.value.copy(
                                priceError = if (!priceValid) "قیمت باید یک عدد مثبت باشد" else null
                        )
                priceValid
            }
            else -> true
        }
    }

    private fun updateName(name: String) {
        _uiState.value =
                _uiState.value.copy(
                        name = name,
                        nameError = if (name.isBlank()) "نام اشتراک نمی‌تواند خالی باشد" else null
                )
    }

    private fun updatePrice(price: String) {
        _uiState.value =
                _uiState.value.copy(
                        price = price,
                        priceError =
                                if (price.isBlank() ||
                                                price.toDoubleOrNull() == null ||
                                                price.toDouble() <= 0
                                )
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
        val nameError =
                if (_uiState.value.name.isBlank()) "نام اشتراک نمی‌تواند خالی باشد" else null
        val priceError =
                if (_uiState.value.price.isBlank() ||
                                _uiState.value.price.toDoubleOrNull() == null ||
                                _uiState.value.price.toDouble() <= 0
                )
                        "قیمت باید یک عدد مثبت باشد"
                else null

        _uiState.value = _uiState.value.copy(nameError = nameError, priceError = priceError)

        return nameError == null && priceError == null
    }

    private fun saveSubscription() {
        if (!validateAndSave()) return

        val newSubscription =
                Subscription(
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
                _uiState.value = _uiState.value.copy(isSaving = false, saveSuccess = true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isSaving = false, error = e.message)
            }
        }
    }

    private fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    private fun resetSaveSuccess() {
        // Reset form but keep subscriptions and presets
        _uiState.value =
                AddSubscriptionUiState(
                        subscriptions = _uiState.value.subscriptions,
                        servicePresets = _uiState.value.servicePresets
                )
    }

    /** انتخاب پریست و پر کردن خودکار فیلدهای فرم Select preset and auto-fill form fields */
    private fun onPresetSelected(preset: ServicePreset) {
        _uiState.value =
                _uiState.value.copy(
                        selectedPreset = preset,
                        name = preset.name,
                        category = preset.defaultCategory,
                        billingCycle = preset.defaultBillingCycle,
                        price = preset.defaultPrice?.toInt()?.toString() ?: "",
                        nameError = null
                )
    }

    /** پاک کردن پریست انتخاب شده Clear selected preset */
    private fun onPresetCleared() {
        _uiState.value = _uiState.value.copy(selectedPreset = null)
    }
}
