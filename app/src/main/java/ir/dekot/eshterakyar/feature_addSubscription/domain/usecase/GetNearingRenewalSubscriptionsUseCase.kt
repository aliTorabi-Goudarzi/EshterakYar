package ir.dekot.eshterakyar.feature_addSubscription.domain.usecase

import ir.dekot.eshterakyar.feature_addSubscription.domain.repository.SubscriptionRepository
import java.util.Date

class GetNearingRenewalSubscriptionsUseCase(
    private val repository: SubscriptionRepository
) {
    suspend operator fun invoke(): Int {
        // Get subscriptions that are due for renewal in the next 3 days
        val threeDaysFromNow = Date().time + (3 * 24 * 60 * 60 * 1000)
        return repository.getNearingRenewalSubscriptionsCount(threeDaysFromNow)
    }
}