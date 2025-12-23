package ir.dekot.eshterakyar.feature_addSubscription.domain.model

/**
 * مدل داده برای نمایش توزیع هزینه بر اساس دسته‌بندی
 *
 * @property category دسته‌بندی اشتراک
 * @property totalAmount مجموع هزینه‌ها در این دسته‌بندی
 * @property count تعداد اشتراک‌ها در این دسته‌بندی
 * @property percentage درصد از کل هزینه‌ها
 */
data class CategoryBreakdown(
        val category: SubscriptionCategory,
        val totalAmount: Double,
        val count: Int,
        val percentage: Float
)
