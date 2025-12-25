package ir.dekot.eshterakyar.feature_addSubscription.domain.model

/**
 * مدل پریست سرویس‌های محبوب شامل اطلاعات پیش‌فرض برای سرویس‌های ایرانی و بین‌المللی معروف
 *
 * @property id شناسه یکتای پریست (مثل "filimo", "namava")
 * @property name نام فارسی سرویس
 * @property iconResId آیدی ریسورس آیکون drawable
 * @property colorCode کد رنگ برند به فرمت هگز (#RRGGBB)
 * @property defaultCategory دسته‌بندی پیش‌فرض سرویس
 * @property defaultPrice قیمت پیش‌فرض (اختیاری - ممکن است تغییر کند)
 * @property defaultBillingCycle دوره پرداخت پیش‌فرض
 */
data class ServicePreset(
        val id: String,
        val name: String,
        val iconResId: Int,
        val colorCode: String,
        val defaultCategory: SubscriptionCategory,
        val defaultPrice: Double? = null,
        val defaultBillingCycle: BillingCycle = BillingCycle.MONTHLY
)
