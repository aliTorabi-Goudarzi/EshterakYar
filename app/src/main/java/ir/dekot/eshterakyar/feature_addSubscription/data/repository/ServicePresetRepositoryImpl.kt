package ir.dekot.eshterakyar.feature_addSubscription.data.repository

import ir.dekot.eshterakyar.R
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.BillingCycle
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.ServicePreset
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.SubscriptionCategory
import ir.dekot.eshterakyar.feature_addSubscription.domain.repository.ServicePresetRepository

/** پیاده‌سازی ریپازیتوری پریست‌ها با داده‌های استاتیک شامل سرویس‌های محبوب ایرانی و بین‌المللی */
class ServicePresetRepositoryImpl : ServicePresetRepository {

    /** لیست پریست‌های از پیش تعریف شده شامل سرویس‌های استریم، موسیقی، حمل‌ونقل و ابزارهای کاری */
    override fun getAllPresets(): List<ServicePreset> =
            listOf(
                    // سرویس‌های ویدیو ایرانی
                    ServicePreset(
                            id = "filimo",
                            name = "فیلیمو",
                            iconResId = R.drawable.ic_preset_filimo,
                            colorCode = "#FF5722",
                            defaultCategory = SubscriptionCategory.VIDEO,
                            defaultPrice = 99000.0,
                            defaultBillingCycle = BillingCycle.MONTHLY
                    ),
                    ServicePreset(
                            id = "namava",
                            name = "نماوا",
                            iconResId = R.drawable.ic_preset_namava,
                            colorCode = "#00BCD4",
                            defaultCategory = SubscriptionCategory.VIDEO,
                            defaultPrice = 69000.0,
                            defaultBillingCycle = BillingCycle.MONTHLY
                    ),
                    ServicePreset(
                            id = "telewebion",
                            name = "تلوبیون",
                            iconResId = R.drawable.ic_preset_telewebion,
                            colorCode = "#9C27B0",
                            defaultCategory = SubscriptionCategory.VIDEO,
                            defaultPrice = 49000.0,
                            defaultBillingCycle = BillingCycle.MONTHLY
                    ),

                    // سرویس‌های حمل‌ونقل
                    ServicePreset(
                            id = "snapp",
                            name = "اسنپ",
                            iconResId = R.drawable.ic_preset_snapp,
                            colorCode = "#00C853",
                            defaultCategory = SubscriptionCategory.OTHER,
                            defaultPrice = null, // قیمت متغیر
                            defaultBillingCycle = BillingCycle.MONTHLY
                    ),
                    ServicePreset(
                            id = "tapsi",
                            name = "تپسی",
                            iconResId = R.drawable.ic_preset_tapsi,
                            colorCode = "#FF9800",
                            defaultCategory = SubscriptionCategory.OTHER,
                            defaultPrice = null,
                            defaultBillingCycle = BillingCycle.MONTHLY
                    ),

                    // سرویس‌های اینترنتی
                    ServicePreset(
                            id = "digiplus",
                            name = "دیجی‌پلاس",
                            iconResId = R.drawable.ic_preset_digiplus,
                            colorCode = "#E91E63",
                            defaultCategory = SubscriptionCategory.OTHER,
                            defaultPrice = 59000.0,
                            defaultBillingCycle = BillingCycle.MONTHLY
                    ),

                    // سرویس‌های بین‌المللی محبوب
                    ServicePreset(
                            id = "spotify",
                            name = "اسپاتیفای",
                            iconResId = R.drawable.ic_preset_spotify,
                            colorCode = "#1DB954",
                            defaultCategory = SubscriptionCategory.MUSIC,
                            defaultPrice = null,
                            defaultBillingCycle = BillingCycle.MONTHLY
                    ),
                    ServicePreset(
                            id = "youtube_premium",
                            name = "یوتیوب پریمیوم",
                            iconResId = R.drawable.ic_preset_youtube,
                            colorCode = "#FF0000",
                            defaultCategory = SubscriptionCategory.VIDEO,
                            defaultPrice = null,
                            defaultBillingCycle = BillingCycle.MONTHLY
                    ),
                    ServicePreset(
                            id = "netflix",
                            name = "نتفلیکس",
                            iconResId = R.drawable.ic_preset_netflix,
                            colorCode = "#E50914",
                            defaultCategory = SubscriptionCategory.VIDEO,
                            defaultPrice = null,
                            defaultBillingCycle = BillingCycle.MONTHLY
                    ),

                    // فضای ابری
                    ServicePreset(
                            id = "google_one",
                            name = "گوگل وان",
                            iconResId = R.drawable.ic_preset_google_one,
                            colorCode = "#4285F4",
                            defaultCategory = SubscriptionCategory.CLOUD,
                            defaultPrice = null,
                            defaultBillingCycle = BillingCycle.MONTHLY
                    )
            )
}
