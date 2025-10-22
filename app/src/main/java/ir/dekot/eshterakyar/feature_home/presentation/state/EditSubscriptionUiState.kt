package ir.dekot.eshterakyar.feature_home.presentation.state

import ir.dekot.eshterakyar.feature_addSubscription.domain.model.BillingCycle
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.Subscription
import java.util.Date

data class EditSubscriptionUiState(
    val subscription: Subscription? = null,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val error: String? = null,
    val name: String = "",
    val price: String = "",
    val currency: String = "IRT",
    val billingCycle: BillingCycle = BillingCycle.MONTHLY,
    val nextRenewalDate: Date = Date(),
    val isActive: Boolean = true,
    val nameError: String? = null,
    val priceError: String? = null
)