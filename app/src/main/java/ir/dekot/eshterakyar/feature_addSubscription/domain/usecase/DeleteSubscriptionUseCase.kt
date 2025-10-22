package ir.dekot.eshterakyar.feature_addSubscription.domain.usecase

import ir.dekot.eshterakyar.feature_addSubscription.domain.model.Subscription
import ir.dekot.eshterakyar.feature_addSubscription.domain.repository.SubscriptionRepository

class DeleteSubscriptionUseCase(
    private val repository: SubscriptionRepository
) {
    suspend operator fun invoke(subscription: Subscription) {
        repository.deleteSubscription(subscription)
    }
}