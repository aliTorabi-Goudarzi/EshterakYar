package ir.dekot.eshterakyar.feature_addSubscription.domain.usecase

import io.mockk.every
import io.mockk.mockk
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.BillingCycle
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.ServicePreset
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.SubscriptionCategory
import ir.dekot.eshterakyar.feature_addSubscription.domain.repository.ServicePresetRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** تست‌های واحد برای GetServicePresetsUseCase بررسی صحت دریافت و بازگشت لیست پریست‌ها */
class GetServicePresetsUseCaseTest {

    private val repository: ServicePresetRepository = mockk()
    private val useCase = GetServicePresetsUseCase(repository)

    @Test
    fun `invoke should return all presets from repository`() {
        // Given - لیستی از پریست‌های تستی
        val expectedPresets =
                listOf(
                        ServicePreset(
                                id = "filimo",
                                name = "فیلیمو",
                                iconResId = 0,
                                colorCode = "#FF5722",
                                defaultCategory = SubscriptionCategory.VIDEO,
                                defaultPrice = 99000.0,
                                defaultBillingCycle = BillingCycle.MONTHLY
                        ),
                        ServicePreset(
                                id = "namava",
                                name = "نماوا",
                                iconResId = 0,
                                colorCode = "#00BCD4",
                                defaultCategory = SubscriptionCategory.VIDEO
                        )
                )
        every { repository.getAllPresets() } returns expectedPresets

        // When - فراخوانی UseCase
        val result = useCase()

        // Then - نتیجه باید با لیست مورد انتظار یکسان باشد
        assertEquals(expectedPresets, result)
        assertEquals(2, result.size)
    }

    @Test
    fun `invoke should return empty list when no presets available`() {
        // Given - ریپازیتوری خالی
        every { repository.getAllPresets() } returns emptyList()

        // When
        val result = useCase()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `preset should have correct default billing cycle when not specified`() {
        // Given - پریست بدون تعیین صریح دوره پرداخت
        val preset =
                ServicePreset(
                        id = "test",
                        name = "تست",
                        iconResId = 0,
                        colorCode = "#000000",
                        defaultCategory = SubscriptionCategory.OTHER
                )
        every { repository.getAllPresets() } returns listOf(preset)

        // When
        val result = useCase()

        // Then - دوره پیش‌فرض باید ماهانه باشد
        assertEquals(BillingCycle.MONTHLY, result.first().defaultBillingCycle)
    }
}
