package ir.dekot.eshterakyar.feature_addSubscription.presentation.state

import ir.dekot.eshterakyar.feature_addSubscription.domain.model.BillingCycle
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.SubscriptionCategory
import java.util.Date

data class AddSubscriptionUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val name: String = "",
    val price: String = "",
    val currency: String = "IRT",
    val billingCycle: BillingCycle = BillingCycle.MONTHLY,
    val nextRenewalDate: Date = Date(),
    val isActive: Boolean = true,
    val category: SubscriptionCategory = SubscriptionCategory.OTHER,
    val nameError: String? = null,
    val priceError: String? = null,
    val saveSuccess: Boolean = false
)