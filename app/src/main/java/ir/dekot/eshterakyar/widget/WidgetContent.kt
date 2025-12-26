package ir.dekot.eshterakyar.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import ir.dekot.eshterakyar.MainActivity
import ir.dekot.eshterakyar.core.domain.utils.DateConverter
import java.time.Instant
import java.time.ZoneId
import java.util.Date

/**
 * محتوای اصلی ویجت تمدیدهای آینده
 *
 * @param renewals لیست تمدیدهای آینده
 * @param modifier تغییردهنده‌های ظاهری
 */
@Composable
fun WidgetContent(renewals: List<WidgetRenewalItem>, modifier: GlanceModifier = GlanceModifier) {
    Box(
            modifier =
                    modifier.fillMaxSize()
                            .background(WidgetColors.widgetBackground)
                            .cornerRadius(16.dp)
                            .clickable(actionStartActivity<MainActivity>())
    ) {
        Column(modifier = GlanceModifier.fillMaxSize().padding(12.dp)) {
            // هدر ویجت
            WidgetHeader()

            Spacer(modifier = GlanceModifier.height(8.dp))

            // لیست تمدیدها
            if (renewals.isEmpty()) {
                EmptyStateContent()
            } else {
                renewals.forEach { item ->
                    RenewalItemRow(item = item)
                    Spacer(modifier = GlanceModifier.height(4.dp))
                }
            }
        }
    }
}

/** هدر ویجت با عنوان */
@Composable
private fun WidgetHeader() {
    Row(
            modifier =
                    GlanceModifier.fillMaxWidth()
                            .background(WidgetColors.headerBackground)
                            .cornerRadius(8.dp)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "📅", style = TextStyle(fontSize = 16.sp))
        Spacer(modifier = GlanceModifier.width(8.dp))
        Text(
                text = "تمدیدهای آینده",
                style =
                        TextStyle(
                                color = WidgetColors.textOnHeader,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                        )
        )
    }
}

/**
 * ردیف نمایش یک آیتم تمدید
 *
 * @param item آیتم تمدید
 */
@Composable
private fun RenewalItemRow(item: WidgetRenewalItem) {
    val jalaliDate = formatDateToJalali(item.nextRenewalDate)

    Row(
            modifier =
                    GlanceModifier.fillMaxWidth()
                            .background(WidgetColors.itemBackground)
                            .cornerRadius(6.dp)
                            .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
    ) {
        // نشانگر رنگی
        Spacer(
                modifier =
                        GlanceModifier.size(8.dp)
                                .cornerRadius(4.dp)
                                .background(parseColor(item.colorCode))
        )

        Spacer(modifier = GlanceModifier.width(8.dp))

        // نام اشتراک
        Text(
                text = item.name,
                style =
                        TextStyle(
                                color = WidgetColors.textPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                        ),
                modifier = GlanceModifier.defaultWeight()
        )

        // تاریخ تمدید
        Text(
                text = jalaliDate,
                style = TextStyle(color = WidgetColors.textSecondary, fontSize = 11.sp)
        )
    }
}

/** نمایش حالت خالی وقتی اشتراکی وجود ندارد */
@Composable
private fun EmptyStateContent() {
    Box(modifier = GlanceModifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
                text = "اشتراکی وجود ندارد",
                style = TextStyle(color = WidgetColors.textSecondary, fontSize = 12.sp)
        )
    }
}

/**
 * تبدیل تاریخ میلادی به شمسی فرمت‌شده
 *
 * @param date تاریخ میلادی
 * @return رشته تاریخ شمسی با فرمت YYYY/MM/DD
 */
private fun formatDateToJalali(date: Date): String {
    val localDate = Instant.ofEpochMilli(date.time).atZone(ZoneId.systemDefault()).toLocalDate()
    val jalaliDate = DateConverter.toJalali(localDate)
    return jalaliDate.format("/")
}

/**
 * تبدیل کد رنگ هگز به ColorProvider
 *
 * @param colorCode کد رنگ هگز (مثل #3498db)
 * @return ColorProvider برای استفاده در Glance
 */
private fun parseColor(colorCode: String): ColorProvider {
    return try {
        val color = Color(android.graphics.Color.parseColor(colorCode))
        ColorProvider(color)
    } catch (e: Exception) {
        ColorProvider(Color(0xFF3498DB)) // رنگ پیش‌فرض آبی
    }
}
