package ir.dekot.eshterakyar.feature_addSubscription.domain.repository

import ir.dekot.eshterakyar.feature_addSubscription.domain.model.Subscription
import kotlinx.coroutines.flow.Flow

interface SubscriptionRepository {
    
    // Get all subscriptions
    fun getAllSubscriptions(): Flow<List<Subscription>>

    // Get all subscriptions sorted by creation date (newest first)
    fun getAllSubscriptionsSortedByCreation(): Flow<List<Subscription>>
    
    // Get only active subscriptions
    fun getActiveSubscriptions(): Flow<List<Subscription>>
    
    // Get subscription by ID
    suspend fun getSubscriptionById(id: Long): Subscription?
    
    // Get subscriptions by category
    fun getSubscriptionsByCategory(category: String): Flow<List<Subscription>>
    
    // Get subscriptions by renewal date range
    fun getSubscriptionsByRenewalDateRange(startDate: Long, endDate: Long): Flow<List<Subscription>>
    
    // Get subscriptions that need reminder
    fun getSubscriptionsNeedingReminder(reminderDate: Long): Flow<List<Subscription>>
    
    // Insert new subscription
    suspend fun insertSubscription(subscription: Subscription): Long
    
    // Insert multiple subscriptions
    suspend fun insertSubscriptions(subscriptions: List<Subscription>)
    
    // Update existing subscription
    suspend fun updateSubscription(subscription: Subscription)
    
    // Delete subscription
    suspend fun deleteSubscription(subscription: Subscription)
    
    // Delete subscription by ID
    suspend fun deleteSubscriptionById(id: Long)
    
    // Update subscription status (active/inactive)
    suspend fun updateSubscriptionStatus(id: Long, isActive: Boolean)
    
    // Update renewal date
    suspend fun updateRenewalDate(id: Long, newDate: Long)
    
    // Get count of active subscriptions
    suspend fun getActiveSubscriptionsCount(): Int
    
    // Get total monthly cost
    suspend fun getTotalMonthlyCost(): Double?
    
    // Get monthly subscriptions cost
    suspend fun getMonthlySubscriptionsCost(): Double?
    
    // Get yearly subscriptions cost (converted to monthly)
    suspend fun getYearlySubscriptionsMonthlyCost(): Double?
    
    // Get count of inactive subscriptions
    suspend fun getInactiveSubscriptionsCount(): Int
    
    // Get count of subscriptions nearing renewal
    suspend fun getNearingRenewalSubscriptionsCount(reminderDate: Long): Int
}