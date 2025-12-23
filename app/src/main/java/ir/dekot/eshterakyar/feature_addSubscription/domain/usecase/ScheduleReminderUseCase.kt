package ir.dekot.eshterakyar.feature_addSubscription.domain.usecase

import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import ir.dekot.eshterakyar.core.notification.ReminderWorker
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.Subscription
import java.util.Calendar
import java.util.concurrent.TimeUnit

/**
 * زمان‌بندی یادآوری برای اشتراک
 *
 * این یوزکیس یک ورکر برای نمایش نوتیفیکیشن در زمان مشخص شده ایجاد می‌کند
 */
class ScheduleReminderUseCase(private val workManager: WorkManager) {
    /**
     * زمان‌بندی یادآوری برای اشتراک
     *
     * @param subscription اشتراک مورد نظر
     */
    operator fun invoke(subscription: Subscription) {
        if (!subscription.isActive) {
            return
        }

        val today = Calendar.getInstance()
        val renewalDate = Calendar.getInstance().apply { time = subscription.nextRenewalDate }

        // محاسبه زمان یادآوری (تاریخ تمدید - روزهای یادآوری)
        val reminderDate =
                Calendar.getInstance().apply {
                    time = subscription.nextRenewalDate
                    add(Calendar.DAY_OF_YEAR, -subscription.reminderDays)
                }

        // اگر تاریخ یادآوری در گذشته است، یادآوری نده
        if (reminderDate.before(today)) {
            // اگر تاریخ تمدید در آینده است ولی یادآوری در گذشته است،
            // نوتیفیکیشن فوری بده
            if (renewalDate.after(today)) {
                scheduleImmediateReminder(subscription)
            }
            return
        }

        // محاسبه تأخیر تا زمان یادآوری
        val delayInMillis = reminderDate.timeInMillis - today.timeInMillis

        // ایجاد WorkRequest
        val workRequest =
                OneTimeWorkRequestBuilder<ReminderWorker>()
                        .setInitialDelay(delayInMillis, TimeUnit.MILLISECONDS)
                        .setInputData(
                                workDataOf(ReminderWorker.KEY_SUBSCRIPTION_ID to subscription.id)
                        )
                        .addTag(ReminderWorker.getWorkTag(subscription.id))
                        .build()

        // زمان‌بندی ورکر با سیاست جایگزینی
        workManager.enqueueUniqueWork(
                ReminderWorker.getWorkTag(subscription.id),
                ExistingWorkPolicy.REPLACE,
                workRequest
        )
    }

    /** زمان‌بندی یادآوری فوری (برای اشتراک‌هایی که تاریخ یادآوری گذشته) */
    private fun scheduleImmediateReminder(subscription: Subscription) {
        val workRequest =
                OneTimeWorkRequestBuilder<ReminderWorker>()
                        .setInitialDelay(
                                5,
                                TimeUnit.SECONDS
                        ) // کمی تأخیر برای جلوگیری از اجرای فوری
                        .setInputData(
                                workDataOf(ReminderWorker.KEY_SUBSCRIPTION_ID to subscription.id)
                        )
                        .addTag(ReminderWorker.getWorkTag(subscription.id))
                        .build()

        workManager.enqueueUniqueWork(
                ReminderWorker.getWorkTag(subscription.id),
                ExistingWorkPolicy.REPLACE,
                workRequest
        )
    }
}
