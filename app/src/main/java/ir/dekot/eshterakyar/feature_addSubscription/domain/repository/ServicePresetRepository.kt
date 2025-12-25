package ir.dekot.eshterakyar.feature_addSubscription.domain.repository

import ir.dekot.eshterakyar.feature_addSubscription.domain.model.ServicePreset

/**
 * ریپازیتوری برای دسترسی به پریست‌های سرویس‌های محبوب این اینترفیس در لایه دامین تعریف شده و در
 * لایه دیتا پیاده‌سازی می‌شود
 */
interface ServicePresetRepository {
    /**
     * دریافت لیست تمام پریست‌های موجود
     * @return لیست پریست‌های سرویس‌های محبوب
     */
    fun getAllPresets(): List<ServicePreset>
}
