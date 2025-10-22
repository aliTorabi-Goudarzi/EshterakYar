package ir.dekot.eshterakyar.feature_addSubscription.domain.usecase

import ir.dekot.eshterakyar.feature_addSubscription.domain.repository.SubscriptionRepository

class GetInactiveSubscriptionsUseCase(
    private val repository: SubscriptionRepository
) {
    suspend operator fun invoke(): Int {
        return repository.getInactiveSubscriptionsCount()
    }
}