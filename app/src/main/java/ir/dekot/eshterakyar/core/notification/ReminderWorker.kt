package ir.dekot.eshterakyar.core.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import ir.dekot.eshterakyar.feature_addSubscription.domain.repository.SubscriptionRepository
import java.util.Calendar
import java.util.concurrent.TimeUnit
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * ورکر یادآوری اشتراک
 *
 * این ورکر توسط WorkManager اجرا شده و نوتیفیکیشن یادآوری را نمایش می‌دهد
 */
class ReminderWorker(private val context: Context, workerParams: WorkerParameters) :
        CoroutineWorker(context, workerParams), KoinComponent {

    private val subscriptionRepository: SubscriptionRepository by inject()

    override suspend fun doWork(): Result {
        val subscriptionId = inputData.getLong(KEY_SUBSCRIPTION_ID, -1)

        if (subscriptionId == -1L) {
            return Result.failure()
        }

        return try {
            val subscription = subscriptionRepository.getSubscriptionById(subscriptionId)

            if (subscription == null || !subscription.isActive) {
                // اشتراک حذف شده یا غیرفعال شده
                return Result.success()
            }

            // محاسبه روزهای باقی‌مانده
            val today = Calendar.getInstance()
            val renewalDate = Calendar.getInstance().apply { time = subscription.nextRenewalDate }

            val diffInMillis = renewalDate.timeInMillis - today.timeInMillis
            val daysRemaining = TimeUnit.MILLISECONDS.toDays(diffInMillis).toInt()

            // نمایش نوتیفیکیشن
            NotificationHelper.showRenewalReminder(
                    context = context,
                    subscription = subscription,
                    daysRemaining = daysRemaining.coerceAtLeast(0)
            )

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    companion object {
        const val KEY_SUBSCRIPTION_ID = "subscription_id"

        /** ساخت تگ یونیک برای هر اشتراک */
        fun getWorkTag(subscriptionId: Long): String = "reminder_$subscriptionId"
    }
}
