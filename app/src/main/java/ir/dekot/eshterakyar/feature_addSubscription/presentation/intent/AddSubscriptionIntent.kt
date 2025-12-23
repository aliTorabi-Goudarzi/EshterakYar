package ir.dekot.eshterakyar.feature_addSubscription.presentation.intent

import ir.dekot.eshterakyar.feature_addSubscription.domain.model.BillingCycle
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.Subscription
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.SubscriptionCategory
import java.util.Date

sealed class AddSubscriptionIntent {
    // تغییر نام اشتراک
    // Change subscription name
    data class OnNameChanged(val name: String) : AddSubscriptionIntent()
    
    // تغییر مبلغ
    // Change price
    data class OnPriceChanged(val price: String) : AddSubscriptionIntent()
    
    // تغییر واحد پول
    // Change currency
    data class OnCurrencyChanged(val currency: String) : AddSubscriptionIntent()
    
    // تغییر دوره پرداخت
    // Change billing cycle
    data class OnBillingCycleChanged(val cycle: BillingCycle) : AddSubscriptionIntent()
    
    // تغییر تاریخ تمدید
    // Change next renewal date
    data class OnNextRenewalDateChanged(val date: Date) : AddSubscriptionIntent()
    
    // تغییر وضعیت فعال بودن
    // Change active status
    data class OnActiveStatusChanged(val isActive: Boolean) : AddSubscriptionIntent()
    
    // تغییر دسته بندی
    // Change category
    data class OnCategoryChanged(val category: SubscriptionCategory) : AddSubscriptionIntent()
    
    // انتخاب اشتراک از لیست
    // Select subscription from list
    data class OnSubscriptionClicked(val subscription: Subscription) : AddSubscriptionIntent()

    // بستن باتم‌شیت
    // Dismiss bottom sheet
    data object OnBottomSheetDismissed : AddSubscriptionIntent()

    // کلیک روی دکمه حذف
    // Delete clicked
    data object OnDeleteClicked : AddSubscriptionIntent()

    // تایید حذف
    // Confirm delete
    data object OnDeleteConfirmed : AddSubscriptionIntent()

    // انصراف از حذف
    // Cancel delete
    data object OnDeleteCancelled : AddSubscriptionIntent()

    // کلیک روی ویرایش
    // Edit clicked
    data class OnEditClicked(val subscription: Subscription) : AddSubscriptionIntent()

    // رفتن به مرحله بعد
    // Go to next step
    data object OnNextStep : AddSubscriptionIntent()
    
    // برگشت به مرحله قبل
    // Go to previous step
    data object OnPreviousStep : AddSubscriptionIntent()
    
    // کلیک روی ذخیره
    // Save clicked
    data object OnSaveClicked : AddSubscriptionIntent()
    
    // بستن پیام خطا
    // Dismiss error
    data object OnErrorDismissed : AddSubscriptionIntent()
    
    // بستن پیام موفقیت
    // Dismiss success
    data object OnSuccessDismissed : AddSubscriptionIntent()
}
