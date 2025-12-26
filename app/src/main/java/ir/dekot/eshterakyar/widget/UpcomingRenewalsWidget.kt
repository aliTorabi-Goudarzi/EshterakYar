package ir.dekot.eshterakyar.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import ir.dekot.eshterakyar.feature_home.domain.usecase.GetUpcomingRenewalsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * ویجت تمدیدهای آینده
 *
 * این ویجت ۳ اشتراک با نزدیک‌ترین تاریخ تمدید را نمایش می‌دهد. اندازه پیش‌فرض ویجت ۳x۲ سلول است.
 */
class UpcomingRenewalsWidget : GlanceAppWidget(), KoinComponent {

    private val getUpcomingRenewalsUseCase: GetUpcomingRenewalsUseCase by inject()

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        // بارگذاری داده‌ها از دیتابیس
        val renewals = withContext(Dispatchers.IO) { loadRenewals() }

        provideContent { GlanceTheme { WidgetContent(renewals = renewals) } }
    }

    /**
     * بارگذاری لیست تمدیدهای آینده از دیتابیس
     *
     * @return لیست آیتم‌های ویجت
     */
    private suspend fun loadRenewals(): List<WidgetRenewalItem> {
        return try {
            val subscriptions = getUpcomingRenewalsUseCase(limit = 3)
            subscriptions.map { subscription ->
                WidgetRenewalItem(
                        id = subscription.id,
                        name = subscription.name,
                        nextRenewalDate = subscription.nextRenewalDate,
                        colorCode = subscription.colorCode
                )
            }
        } catch (e: Exception) {
            // در صورت خطا، لیست خالی برگردان
            emptyList()
        }
    }
}
