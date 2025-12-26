package ir.dekot.eshterakyar.core.database

import androidx.room.*
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.Subscription
import kotlinx.coroutines.flow.Flow

@Dao
interface SubscriptionDao {

        @Query("SELECT * FROM subscriptions ORDER BY nextRenewalDate ASC")
        fun getAllSubscriptions(): Flow<List<Subscription>>

        @Query("SELECT * FROM subscriptions ORDER BY id DESC")
        fun getAllSubscriptionsSortedByCreation(): Flow<List<Subscription>>

        @Query("SELECT * FROM subscriptions WHERE isActive = 1 ORDER BY nextRenewalDate ASC")
        fun getActiveSubscriptions(): Flow<List<Subscription>>

        @Query("SELECT * FROM subscriptions WHERE id = :id")
        suspend fun getSubscriptionById(id: Long): Subscription?

        @Query(
                "SELECT * FROM subscriptions WHERE category = :category ORDER BY nextRenewalDate ASC"
        )
        fun getSubscriptionsByCategory(category: String): Flow<List<Subscription>>

        @Query(
                "SELECT * FROM subscriptions WHERE nextRenewalDate BETWEEN :startDate AND :endDate ORDER BY nextRenewalDate ASC"
        )
        fun getSubscriptionsByRenewalDateRange(
                startDate: Long,
                endDate: Long
        ): Flow<List<Subscription>>

        @Query(
                "SELECT * FROM subscriptions WHERE nextRenewalDate <= :reminderDate AND isActive = 1 ORDER BY nextRenewalDate ASC"
        )
        fun getSubscriptionsNeedingReminder(reminderDate: Long): Flow<List<Subscription>>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertSubscription(subscription: Subscription): Long

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertSubscriptions(subscriptions: List<Subscription>)

        @Update suspend fun updateSubscription(subscription: Subscription)

        @Delete suspend fun deleteSubscription(subscription: Subscription)

        @Query("DELETE FROM subscriptions WHERE id = :id")
        suspend fun deleteSubscriptionById(id: Long)

        @Query("UPDATE subscriptions SET isActive = :isActive WHERE id = :id")
        suspend fun updateSubscriptionStatus(id: Long, isActive: Boolean)

        @Query("UPDATE subscriptions SET nextRenewalDate = :newDate WHERE id = :id")
        suspend fun updateRenewalDate(id: Long, newDate: Long)

        @Query("SELECT COUNT(*) FROM subscriptions WHERE isActive = 1")
        suspend fun getActiveSubscriptionsCount(): Int

        @Query("SELECT SUM(price) FROM subscriptions WHERE isActive = 1")
        suspend fun getTotalMonthlyCost(): Double?

        @Query(
                "SELECT SUM(price) FROM subscriptions WHERE isActive = 1 AND billingCycle = 'MONTHLY'"
        )
        suspend fun getMonthlySubscriptionsCost(): Double?

        @Query(
                "SELECT SUM(price * 12 / 365) FROM subscriptions WHERE isActive = 1 AND billingCycle = 'YEARLY'"
        )
        suspend fun getYearlySubscriptionsMonthlyCost(): Double?

        @Query("SELECT COUNT(*) FROM subscriptions WHERE isActive = 0")
        suspend fun getInactiveSubscriptionsCount(): Int

        @Query(
                "SELECT COUNT(*) FROM subscriptions WHERE isActive = 1 AND nextRenewalDate <= :reminderDate"
        )
        suspend fun getNearingRenewalSubscriptionsCount(reminderDate: Long): Int

        @Query("SELECT MIN(nextRenewalDate) FROM subscriptions WHERE isActive = 1")
        suspend fun getNearestRenewalDate(): Long?

        /** بررسی تکراری بودن نام اشتراک */
        @Query("SELECT COUNT(*) FROM subscriptions WHERE name = :name")
        suspend fun countByName(name: String): Int

        /** بررسی تکراری بودن نام اشتراک (به جز اشتراک خاص - برای ویرایش) */
        @Query("SELECT COUNT(*) FROM subscriptions WHERE name = :name AND id != :excludeId")
        suspend fun countByNameExcludingId(name: String, excludeId: Long): Int
}
