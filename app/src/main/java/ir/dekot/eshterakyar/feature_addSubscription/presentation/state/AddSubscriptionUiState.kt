package ir.dekot.eshterakyar.feature_addSubscription.presentation.state

import ir.dekot.eshterakyar.feature_addSubscription.domain.model.BillingCycle
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.SubscriptionCategory
import java.util.Date

data class AddSubscriptionUiState(
    // وضعیت بارگذاری
    // Loading status
    val isLoading: Boolean = false,
    
    // وضعیت ذخیره سازی
    // Saving status
    val isSaving: Boolean = false,
    
    // پیام خطا
    // Error message
    val error: String? = null,
    
    // نام اشتراک
    // Subscription name
    val name: String = "",
    
    // مبلغ
    // Price
    val price: String = "",
    
    // واحد پول
    // Currency
    val currency: String = "IRT",
    
    // دوره پرداخت
    // Billing cycle
    val billingCycle: BillingCycle = BillingCycle.MONTHLY,
    
    // تاریخ تمدید بعدی
    // Next renewal date
    val nextRenewalDate: Date = Date(),
    
    // وضعیت فعال بودن
    // Active status
    val isActive: Boolean = true,
    
    // دسته بندی
    // Category
    val category: SubscriptionCategory = SubscriptionCategory.OTHER,
    
    // خطای نام اشتراک
    // Name error
    val nameError: String? = null,
    
    // خطای مبلغ
    // Price error
    val priceError: String? = null,
    
    // وضعیت موفقیت ذخیره سازی
    // Save success status
    val saveSuccess: Boolean = false,

    // مرحله فعلی فرم (از ۱ شروع می‌شود)
    // Current form step (starts from 1)
    val currentStep: Int = 1
)
