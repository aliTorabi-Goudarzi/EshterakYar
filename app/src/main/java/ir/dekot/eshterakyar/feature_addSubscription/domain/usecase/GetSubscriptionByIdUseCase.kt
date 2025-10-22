package ir.dekot.eshterakyar.feature_addSubscription.domain.usecase

import ir.dekot.eshterakyar.feature_addSubscription.domain.model.Subscription
import ir.dekot.eshterakyar.feature_addSubscription.domain.repository.SubscriptionRepository

class GetSubscriptionByIdUseCase(
    private val repository: SubscriptionRepository
) {
    suspend operator fun invoke(id: Long): Subscription? {
        return repository.getSubscriptionById(id)
    }
}