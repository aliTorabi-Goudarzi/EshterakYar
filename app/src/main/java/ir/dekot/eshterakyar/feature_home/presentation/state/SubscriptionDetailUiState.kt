package ir.dekot.eshterakyar.feature_home.presentation.state

import ir.dekot.eshterakyar.feature_addSubscription.domain.model.Subscription

data class SubscriptionDetailUiState(
    val subscription: Subscription? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val isDeleting: Boolean = false
)