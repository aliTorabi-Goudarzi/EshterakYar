package ir.dekot.eshterakyar.feature_addSubscription.domain.usecase

import ir.dekot.eshterakyar.feature_addSubscription.domain.repository.SubscriptionRepository
import java.util.Date

data class SubscriptionStats(
        val activeCount: Int,
        val totalMonthlyCost: Double,
        val monthlyCost: Double,
        val yearlyMonthlyCost: Double,
        val nearestRenewalDate: Date?
)

class GetSubscriptionStatsUseCase(private val repository: SubscriptionRepository) {
    suspend operator fun invoke(): SubscriptionStats {
        val activeCount = repository.getActiveSubscriptionsCount()
        val totalMonthlyCost = repository.getTotalMonthlyCost() ?: 0.0
        val monthlyCost = repository.getMonthlySubscriptionsCost() ?: 0.0
        val yearlyMonthlyCost = repository.getYearlySubscriptionsMonthlyCost() ?: 0.0
        val nearestRenewalTimestamp = repository.getNearestRenewalDate()
        val nearestRenewalDate = nearestRenewalTimestamp?.let { Date(it) }

        return SubscriptionStats(
                activeCount = activeCount,
                totalMonthlyCost = totalMonthlyCost,
                monthlyCost = monthlyCost,
                yearlyMonthlyCost = yearlyMonthlyCost,
                nearestRenewalDate = nearestRenewalDate
        )
    }
}
