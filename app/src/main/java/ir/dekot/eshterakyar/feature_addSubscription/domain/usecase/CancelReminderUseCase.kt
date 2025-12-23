package ir.dekot.eshterakyar.feature_addSubscription.domain.usecase

import androidx.work.WorkManager
import ir.dekot.eshterakyar.core.notification.ReminderWorker

/**
 * لغو یادآوری اشتراک
 *
 * این یوزکیس ورکر یادآوری را لغو می‌کند (مثلاً هنگام حذف اشتراک)
 */
class CancelReminderUseCase(private val workManager: WorkManager) {
    /**
     * لغو یادآوری برای اشتراک
     *
     * @param subscriptionId شناسه اشتراک
     */
    operator fun invoke(subscriptionId: Long) {
        workManager.cancelUniqueWork(ReminderWorker.getWorkTag(subscriptionId))
    }
}
