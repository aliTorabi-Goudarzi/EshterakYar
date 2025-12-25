package ir.dekot.eshterakyar.feature_home.presentation.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.dekot.eshterakyar.core.utils.LocalTheme
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.SubscriptionStats
import sv.lib.squircleshape.CornerSmoothing
import sv.lib.squircleshape.SquircleShape

@Composable
fun StatsCard(stats: SubscriptionStats, modifier: Modifier = Modifier) {
    val theme = LocalTheme.current
    var animatedProgress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(stats.totalMonthlyCost) { animatedProgress = 1f }

    val progressAnimation by
            animateFloatAsState(
                    targetValue = animatedProgress,
                    animationSpec = tween(durationMillis = 1000),
                    label = "progress"
            )

    Card(
            modifier = modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = theme.primaryContainer),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = SquircleShape(radius = 20.dp, smoothing = CornerSmoothing.Medium)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            // Header
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                        text = "آمار اشتراک‌ها",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = theme.onPrimaryContainer
                )

                Icon(
                        imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                        contentDescription = "Stats",
                        tint = theme.onPrimaryContainer,
                        modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Stats grid
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Active subscriptions count
                StatItem(
                        title = "اشتراک‌های فعال",
                        value = stats.activeCount.toString(),
                        color = theme.primary
                )

                // Monthly cost
                StatItem(
                        title = "هزینه ماهانه",
                        value = formatPrice(stats.totalMonthlyCost),
                        color = theme.secondary
                )

                // Nearest renewal date
                StatItem(
                        title = "نزدیک‌ترین تمدید",
                        value =
                                stats.nearestRenewalDate?.let {
                                    val localDate =
                                            it.toInstant()
                                                    .atZone(java.time.ZoneId.systemDefault())
                                                    .toLocalDate()
                                    val jalaliDate =
                                            ir.dekot.eshterakyar.core.domain.utils.DateConverter
                                                    .toJalali(localDate)
                                    "${jalaliDate.day} ${jalaliDate.monthName()}"
                                }
                                        ?: "—",
                        color = theme.tertiary
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Progress bar with animation
            Column {
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                            text = "محدود بودجه ماهانه",
                            style = MaterialTheme.typography.bodyMedium,
                            color = theme.onPrimaryContainer
                    )

                    Text(
                            text = "${(stats.totalMonthlyCost * 100 / 500000).toInt()}%",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = theme.onPrimaryContainer
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                        modifier =
                                Modifier.fillMaxWidth()
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(theme.onPrimaryContainer.copy(alpha = 0.2f))
                ) {
                    LinearProgressIndicator(
                            progress = {
                                (progressAnimation *
                                                (stats.totalMonthlyCost / 500000.0).coerceAtMost(
                                                        1.0
                                                ))
                                        .toFloat()
                            },
                            modifier =
                                    Modifier.fillMaxWidth()
                                            .height(8.dp)
                                            .clip(RoundedCornerShape(4.dp)),
                            color =
                                    if (stats.totalMonthlyCost > 500000) Color.Red
                                    else theme.primary,
                            trackColor = Color.Transparent
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                        text = "بودجه: ۵۰۰,۰۰۰ تومان",
                        style = MaterialTheme.typography.bodySmall,
                        color = theme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Cost breakdown
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CostBreakdownItem(
                        title = "ماهانه",
                        value = stats.monthlyCost,
                        percentage = (stats.monthlyCost / stats.totalMonthlyCost * 100).toInt(),
                        color = theme.primary
                )

                CostBreakdownItem(
                        title = "سالانه",
                        value = stats.yearlyMonthlyCost,
                        percentage =
                                (stats.yearlyMonthlyCost / stats.totalMonthlyCost * 100).toInt(),
                        color = theme.secondary
                )
            }
        }
    }
}

@Composable
private fun StatItem(title: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = color,
                fontSize = 22.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = color.copy(alpha = 0.8f)
        )
    }
}

@Composable
private fun CostBreakdownItem(title: String, value: Double, percentage: Int, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(12.dp).clip(RoundedCornerShape(6.dp)).background(color))

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                    text = "$title: ${formatPrice(value)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = color
            )

            Text(
                    text = "$percentage% از کل",
                    style = MaterialTheme.typography.bodySmall,
                    color = color.copy(alpha = 0.7f)
            )
        }
    }
}

private fun formatPrice(price: Double): String {
    return "${price.toInt()} تومان"
}
