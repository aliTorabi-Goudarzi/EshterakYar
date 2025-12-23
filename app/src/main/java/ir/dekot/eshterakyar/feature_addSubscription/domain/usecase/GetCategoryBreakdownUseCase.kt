package ir.dekot.eshterakyar.feature_addSubscription.domain.usecase

import ir.dekot.eshterakyar.feature_addSubscription.domain.model.CategoryBreakdown
import ir.dekot.eshterakyar.feature_addSubscription.domain.repository.SubscriptionRepository

/**
 * محاسبه توزیع هزینه‌ها بر اساس دسته‌بندی
 *
 * این یوزکیس لیست اشتراک‌های فعال را دریافت کرده و توزیع هزینه‌ها را بر اساس دسته‌بندی محاسبه
 * می‌کند
 */
class GetCategoryBreakdownUseCase(private val repository: SubscriptionRepository) {
    /**
     * محاسبه توزیع هزینه‌ها
     *
     * @return لیست توزیع هزینه به تفکیک دسته‌بندی، مرتب شده بر اساس مبلغ (نزولی)
     */
    suspend operator fun invoke(): List<CategoryBreakdown> {
        val subscriptions = repository.getActiveSubscriptionsList()

        if (subscriptions.isEmpty()) {
            return emptyList()
        }

        // گروه‌بندی بر اساس دسته‌بندی
        val groupedByCategory = subscriptions.groupBy { it.category }

        // محاسبه مجموع کل
        val totalAmount = subscriptions.sumOf { it.price }

        // ساخت لیست توزیع
        return groupedByCategory
                .map { (category, subs) ->
                    val categoryTotal = subs.sumOf { it.price }
                    CategoryBreakdown(
                            category = category,
                            totalAmount = categoryTotal,
                            count = subs.size,
                            percentage =
                                    if (totalAmount > 0) {
                                        (categoryTotal / totalAmount * 100).toFloat()
                                    } else {
                                        0f
                                    }
                    )
                }
                .sortedByDescending { it.totalAmount }
    }
}
