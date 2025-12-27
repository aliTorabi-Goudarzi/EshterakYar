package ir.dekot.eshterakyar.feature_home.presentation.mvi

import androidx.compose.runtime.Immutable
import ir.dekot.eshterakyar.core.preferences.BudgetPreferences
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.Subscription
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.SubscriptionStats
import ir.dekot.eshterakyar.feature_home.domain.model.UserGreeting

/** گزینه‌های مرتب‌سازی لیست اشتراک‌ها */
enum class SortOption(val persianName: String) {
    RENEWAL_SOON("نزدیک‌ترین تمدید"),
    RENEWAL_FAR("دورترین تمدید"),
    PRICE_HIGH("گران‌ترین"),
    PRICE_LOW("ارزان‌ترین"),
    NAME_ASC("نام (الف تا ی)"),
    NAME_DESC("نام (ی تا الف)")
}

/** قرارداد وضعیت، قصد و اثرات جانبی برای صفحه خانه */

/**
 * وضعیت UI صفحه خانه
 *
 * @param subscriptions لیست اشتراک‌های فعال
 * @param stats آمار مربوط به اشتراک‌ها
 * @param greeting پیام خوش‌آمدگویی به کاربر
 * @param inactiveCount تعداد اشتراک‌های غیرفعال
 * @param nearingRenewalCount تعداد اشتراک‌های در حال تمدید
 * @param budget بودجه ماهانه کاربر
 * @param isLoading وضعیت بارگذاری داده‌ها
 * @param error پیام خطا در صورت بروز مشکل
 */
@Immutable
data class HomeState(
        val subscriptions: List<Subscription> = emptyList(),
        val filteredSubscriptions: List<Subscription> = emptyList(),
        val searchQuery: String = "",
        val selectedSortOption: SortOption = SortOption.RENEWAL_SOON,
        val stats: SubscriptionStats? = null,
        val greeting: UserGreeting? = null,
        val inactiveCount: Int = 0,
        val nearingRenewalCount: Int = 0,
        val budget: Double = BudgetPreferences.DEFAULT_BUDGET,
        val isLoading: Boolean = true,
        val error: String? = null
)

/** قصد‌های (Intents) کاربر در صفحه خانه */
sealed interface HomeIntent {
    /** بارگذاری مجدد داده‌ها */
    data object Refresh : HomeIntent

    /** تغییر متن جستجو */
    data class OnSearchQueryChanged(val query: String) : HomeIntent

    /** تغییر نوع مرتب‌سازی */
    data class OnSortOptionChanged(val option: SortOption) : HomeIntent
    data object OnAddSubscriptionClicked : HomeIntent

    /**
     * کلیک روی یک اشتراک خاص
     * @param id شناسه اشتراک
     */
    data class OnSubscriptionClicked(val id: Long) : HomeIntent

    /**
     * کلیک روی دکمه ویرایش اشتراک
     * @param id شناسه اشتراک
     */
    data class OnEditSubscriptionClicked(val id: Long) : HomeIntent

    /**
     * درخواست حذف اشتراک
     * @param subscription اشتراک مورد نظر برای حذف
     */
    data class OnDeleteSubscription(val subscription: Subscription) : HomeIntent

    /**
     * تغییر وضعیت اشتراک (فعال/غیرفعال)
     * @param subscription اشتراک مورد نظر
     */
    data class OnToggleSubscriptionStatus(val subscription: Subscription) : HomeIntent

    /**
     * ثبت پرداخت دستی (دکمه "پرداخت کردم")
     * @param subscription اشتراک مورد نظر
     */
    data class OnPaymentConfirmed(val subscription: Subscription) : HomeIntent
}

/** اثرات جانبی (Side Effects) صفحه خانه مثل نویگیشن یا نمایش پیام‌های موقت */
sealed interface HomeEffect {
    /** هدایت به صفحه افزودن اشتراک */
    data object NavigateToAddSubscription : HomeEffect

    /**
     * هدایت به صفحه جزئیات اشتراک
     * @param id شناسه اشتراک
     */
    data class NavigateToSubscriptionDetail(val id: Long) : HomeEffect

    /**
     * هدایت به صفحه ویرایش اشتراک
     * @param id شناسه اشتراک
     */
    data class NavigateToEditSubscription(val id: Long) : HomeEffect

    /**
     * نمایش پیام خطا یا اطلاع‌رسانی به کاربر (مثلاً اسنک‌بار)
     * @param message متن پیام
     */
    data class ShowMessage(val message: String) : HomeEffect
}
