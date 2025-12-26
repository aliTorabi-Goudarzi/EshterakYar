package ir.dekot.eshterakyar.feature_home.presentation.state

import ir.dekot.eshterakyar.feature_addSubscription.domain.model.Subscription
import ir.dekot.eshterakyar.feature_payment.data.model.PaymentLog

data class SubscriptionDetailUiState(
        val subscription: Subscription? = null,
        val paymentLogs: List<PaymentLog> = emptyList(),
        val isLoading: Boolean = true,
        val error: String? = null,
        val isDeleting: Boolean = false
)
