package ir.dekot.eshterakyar.feature_addSubscription.domain.usecase

import ir.dekot.eshterakyar.feature_addSubscription.domain.model.ServicePreset
import ir.dekot.eshterakyar.feature_addSubscription.domain.repository.ServicePresetRepository

/**
 * UseCase برای دریافت لیست پریست‌های سرویس‌های محبوب از این UseCase در ویومدل برای نمایش لیست
 * پریست‌ها استفاده می‌شود
 *
 * @property repository ریپازیتوری پریست‌ها
 */
class GetServicePresetsUseCase(private val repository: ServicePresetRepository) {
    /**
     * دریافت تمام پریست‌های موجود
     * @return لیست پریست‌های سرویس‌های محبوب
     */
    operator fun invoke(): List<ServicePreset> {
        return repository.getAllPresets()
    }
}
