package ir.dekot.eshterakyar.feature_home.domain.usecase

import ir.dekot.eshterakyar.feature_addSubscription.domain.model.Subscription
import ir.dekot.eshterakyar.feature_addSubscription.domain.repository.SubscriptionRepository

/**
 * یوزکیس برای دریافت اشتراک‌های فعال با نزدیک‌ترین تاریخ تمدید
 *
 * این یوزکیس برای ویجت صفحه اصلی استفاده می‌شود و
 * تعداد محدودی از اشتراک‌ها را برمی‌گرداند
 *
 * @param repository ریپازیتوری اشتراک‌ها
 */
class GetUpcomingRenewalsUseCase(
    private val repository: SubscriptionRepository
) {
    /**
     * دریافت لیست اشتراک‌های فعال با نزدیک‌ترین تاریخ تمدید
     *
     * @param limit تعداد اشتراک‌هایی که باید برگردانده شوند (پیش‌فرض: ۳)
     * @return لیست اشتراک‌ها مرتب شده بر اساس تاریخ تمدید
     */
    suspend operator fun invoke(limit: Int = 3): List<Subscription> {
        val activeSubscriptions = repository.getActiveSubscriptionsList()
        return activeSubscriptions
            .sortedBy { it.nextRenewalDate }
            .take(limit)
    }
}
