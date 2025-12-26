package ir.dekot.eshterakyar.widget

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

/**
 * ریسیور ویجت تمدیدهای آینده
 *
 * این کلاس نقطه ورود ویجت است و توسط سیستم اندروید برای مدیریت رویدادهای ویجت استفاده می‌شود.
 */
class UpcomingRenewalsWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = UpcomingRenewalsWidget()
}
