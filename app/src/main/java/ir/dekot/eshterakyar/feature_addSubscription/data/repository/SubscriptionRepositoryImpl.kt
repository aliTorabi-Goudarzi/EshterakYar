package ir.dekot.eshterakyar.feature_addSubscription.data.repository

import ir.dekot.eshterakyar.core.database.SubscriptionDao
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.Subscription
import ir.dekot.eshterakyar.feature_addSubscription.domain.repository.SubscriptionRepository
import kotlinx.coroutines.flow.Flow

class SubscriptionRepositoryImpl(
    private val subscriptionDao: SubscriptionDao
) : SubscriptionRepository {
    
    override fun getAllSubscriptions(): Flow<List<Subscription>> {
        return subscriptionDao.getAllSubscriptions()
    }
    
    override fun getActiveSubscriptions(): Flow<List<Subscription>> {
        return subscriptionDao.getActiveSubscriptions()
    }
    
    override suspend fun getSubscriptionById(id: Long): Subscription? {
        return subscriptionDao.getSubscriptionById(id)
    }
    
    override fun getSubscriptionsByCategory(category: String): Flow<List<Subscription>> {
        return subscriptionDao.getSubscriptionsByCategory(category)
    }
    
    override fun getSubscriptionsByRenewalDateRange(startDate: Long, endDate: Long): Flow<List<Subscription>> {
        return subscriptionDao.getSubscriptionsByRenewalDateRange(startDate, endDate)
    }
    
    override fun getSubscriptionsNeedingReminder(reminderDate: Long): Flow<List<Subscription>> {
        return subscriptionDao.getSubscriptionsNeedingReminder(reminderDate)
    }
    
    override suspend fun insertSubscription(subscription: Subscription): Long {
        return subscriptionDao.insertSubscription(subscription)
    }
    
    override suspend fun insertSubscriptions(subscriptions: List<Subscription>) {
        subscriptionDao.insertSubscriptions(subscriptions)
    }
    
    override suspend fun updateSubscription(subscription: Subscription) {
        subscriptionDao.updateSubscription(subscription)
    }
    
    override suspend fun deleteSubscription(subscription: Subscription) {
        subscriptionDao.deleteSubscription(subscription)
    }
    
    override suspend fun deleteSubscriptionById(id: Long) {
        subscriptionDao.deleteSubscriptionById(id)
    }
    
    override suspend fun updateSubscriptionStatus(id: Long, isActive: Boolean) {
        subscriptionDao.updateSubscriptionStatus(id, isActive)
    }
    
    override suspend fun updateRenewalDate(id: Long, newDate: Long) {
        subscriptionDao.updateRenewalDate(id, newDate)
    }
    
    override suspend fun getActiveSubscriptionsCount(): Int {
        return subscriptionDao.getActiveSubscriptionsCount()
    }
    
    override suspend fun getTotalMonthlyCost(): Double? {
        val monthlyCost = subscriptionDao.getMonthlySubscriptionsCost() ?: 0.0
        val yearlyMonthlyCost = subscriptionDao.getYearlySubscriptionsMonthlyCost() ?: 0.0
        return monthlyCost + yearlyMonthlyCost
    }
    
    override suspend fun getMonthlySubscriptionsCost(): Double? {
        return subscriptionDao.getMonthlySubscriptionsCost()
    }
    
    override suspend fun getYearlySubscriptionsMonthlyCost(): Double? {
        return subscriptionDao.getYearlySubscriptionsMonthlyCost()
    }
    
    override suspend fun getInactiveSubscriptionsCount(): Int {
        return subscriptionDao.getInactiveSubscriptionsCount()
    }
    
    override suspend fun getNearingRenewalSubscriptionsCount(reminderDate: Long): Int {
        return subscriptionDao.getNearingRenewalSubscriptionsCount(reminderDate)
    }
}