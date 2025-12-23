package ir.dekot.eshterakyar.screens.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ir.dekot.eshterakyar.core.utils.CurrencyConverter
import ir.dekot.eshterakyar.core.utils.LocalTheme
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.CategoryBreakdown
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.SubscriptionCategory

/** رنگ‌های مخصوص هر دسته‌بندی */
private val categoryColors =
        mapOf(
                SubscriptionCategory.ENTERTAINMENT to Color(0xFFE91E63),
                SubscriptionCategory.PRODUCTIVITY to Color(0xFF2196F3),
                SubscriptionCategory.EDUCATION to Color(0xFF4CAF50),
                SubscriptionCategory.HEALTH to Color(0xFFFF5722),
                SubscriptionCategory.NEWS to Color(0xFF9C27B0),
                SubscriptionCategory.MUSIC to Color(0xFF00BCD4),
                SubscriptionCategory.VIDEO to Color(0xFFF44336),
                SubscriptionCategory.CLOUD to Color(0xFF3F51B5),
                SubscriptionCategory.OTHER to Color(0xFF607D8B)
        )

/**
 * کارت نمودار توزیع هزینه‌ها بر اساس دسته‌بندی
 *
 * @param breakdowns لیست توزیع هزینه‌ها
 * @param modifier مادیفایر
 * @param currencyCode کد ارز برای نمایش
 */
@Composable
fun CategoryBreakdownChart(
        breakdowns: List<CategoryBreakdown>,
        modifier: Modifier = Modifier,
        currencyCode: String = "IRT"
) {
        val theme = LocalTheme.current

        Card(
                modifier = modifier,
                colors = CardDefaults.cardColors(containerColor = theme.surfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        // عنوان
                        Text(
                                text = "توزیع هزینه‌ها بر اساس دسته‌بندی",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = theme.onSurface
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        if (breakdowns.isEmpty()) {
                                // وضعیت خالی
                                Box(
                                        modifier = Modifier.fillMaxWidth().height(150.dp),
                                        contentAlignment = Alignment.Center
                                ) {
                                        Text(
                                                text = "اشتراکی برای نمایش وجود ندارد",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = theme.onSurfaceVariant,
                                                textAlign = TextAlign.Center
                                        )
                                }
                        } else {
                                // نمودار میله‌ای افقی سفارشی
                                CategoryHorizontalBarChart(breakdowns = breakdowns)

                                Spacer(modifier = Modifier.height(16.dp))

                                // Legend (افسانه نمودار)
                                CategoryLegend(breakdowns = breakdowns, currencyCode = currencyCode)
                        }
                }
        }
}

/**
 * نمودار میله‌ای افقی سفارشی
 *
 * نمایش درصد هر دسته‌بندی به صورت نوار افقی با انیمیشن
 */
@Composable
private fun CategoryHorizontalBarChart(
        breakdowns: List<CategoryBreakdown>,
        modifier: Modifier = Modifier
) {
        val theme = LocalTheme.current

        Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                breakdowns.forEach { breakdown ->
                        val animatedProgress by
                                animateFloatAsState(
                                        targetValue = breakdown.percentage / 100f,
                                        animationSpec = tween(durationMillis = 800),
                                        label = "bar_progress_${breakdown.category.name}"
                                )

                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                                // نام دسته‌بندی
                                Text(
                                        text = breakdown.category.persianName,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = theme.onSurface,
                                        modifier = Modifier.width(70.dp)
                                )

                                // نوار پیشرفت
                                Box(
                                        modifier =
                                                Modifier.weight(1f)
                                                        .height(20.dp)
                                                        .clip(RoundedCornerShape(10.dp))
                                                        .background(theme.surface)
                                ) {
                                        Box(
                                                modifier =
                                                        Modifier.fillMaxWidth(animatedProgress)
                                                                .height(20.dp)
                                                                .clip(RoundedCornerShape(10.dp))
                                                                .background(
                                                                        categoryColors[
                                                                                breakdown.category]
                                                                                ?: theme.primary
                                                                )
                                        )
                                }

                                // درصد
                                Text(
                                        text = "${String.format("%.0f", breakdown.percentage)}%",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = theme.onSurface,
                                        modifier = Modifier.width(35.dp)
                                )
                        }
                }
        }
}

/** افسانه نمودار با رنگ و مبلغ هر دسته‌بندی */
@Composable
private fun CategoryLegend(breakdowns: List<CategoryBreakdown>, currencyCode: String) {
        val theme = LocalTheme.current

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                breakdowns.forEach { breakdown ->
                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                                // رنگ و نام دسته‌بندی
                                Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                        Box(
                                                modifier =
                                                        Modifier.size(12.dp)
                                                                .clip(CircleShape)
                                                                .background(
                                                                        categoryColors[
                                                                                breakdown.category]
                                                                                ?: Color.Gray
                                                                )
                                        )
                                        Text(
                                                text = breakdown.category.persianName,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = theme.onSurface
                                        )
                                        Text(
                                                text = "(${breakdown.count})",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = theme.onSurfaceVariant
                                        )
                                }

                                // مبلغ
                                Text(
                                        text =
                                                CurrencyConverter.formatPrice(
                                                        breakdown.totalAmount,
                                                        currencyCode
                                                ),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium,
                                        color = theme.onSurface
                                )
                        }
                }
        }
}
