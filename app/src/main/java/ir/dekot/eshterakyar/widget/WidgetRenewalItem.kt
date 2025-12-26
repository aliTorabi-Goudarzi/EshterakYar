package ir.dekot.eshterakyar.widget

import java.util.Date

/**
 * مدل داده‌ای ساده برای نمایش در ویجت
 *
 * این مدل فقط اطلاعات مورد نیاز برای نمایش در ویجت را شامل می‌شود
 *
 * @param id شناسه اشتراک
 * @param name نام اشتراک
 * @param nextRenewalDate تاریخ تمدید بعدی
 * @param colorCode کد رنگ اشتراک
 */
data class WidgetRenewalItem(
    val id: Long,
    val name: String,
    val nextRenewalDate: Date,
    val colorCode: String
)
