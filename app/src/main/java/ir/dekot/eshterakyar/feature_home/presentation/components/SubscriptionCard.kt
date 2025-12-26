package ir.dekot.eshterakyar.feature_home.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import ir.dekot.eshterakyar.core.utils.CurrencyConverter
import ir.dekot.eshterakyar.core.utils.HapticHelper
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.Subscription
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.SubscriptionCategory
import java.util.Date
import kotlinx.coroutines.delay
import sv.lib.squircleshape.CornerSmoothing
import sv.lib.squircleshape.SquircleShape

@Composable
fun SubscriptionCard(
        subscription: Subscription,
        modifier: Modifier = Modifier,
        onClick: () -> Unit = {},
        onEdit: () -> Unit = {},
        onDelete: () -> Unit = {},
        onToggleStatus: () -> Unit = {},
        onPaymentConfirm: () -> Unit = {}
) {
        var expanded by remember { mutableStateOf(false) }
        val view = LocalView.current

        // انیمیشن scale برای micro-interaction
        var isAnimating by remember { mutableStateOf(false) }
        val scale by
                animateFloatAsState(
                        targetValue = if (isAnimating) 1.05f else 1f,
                        animationSpec =
                                spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessMedium
                                ),
                        label = "cardScale"
                )

        // Reset animation after a short delay
        LaunchedEffect(isAnimating) {
                if (isAnimating) {
                        delay(150)
                        isAnimating = false
                }
        }

        Card(
                modifier =
                        modifier.fillMaxWidth().scale(scale).clickable {
                                HapticHelper.performClick(view)
                                onClick()
                        },
                colors =
                        CardDefaults.cardColors(
                                containerColor =
                                        if (subscription.isActive) MaterialTheme.colorScheme.surface
                                        else
                                                MaterialTheme.colorScheme.surfaceVariant.copy(
                                                        alpha = 0.7f
                                                )
                        ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = SquircleShape(radius = 20.dp, smoothing = CornerSmoothing.Medium)
        ) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        // Header with icon and menu
                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                                // Icon and name
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                        // Icon placeholder
                                        Box(
                                                modifier =
                                                        Modifier.size(48.dp)
                                                                .clip(CircleShape)
                                                                .background(
                                                                        color =
                                                                                Color(
                                                                                        subscription
                                                                                                .colorCode
                                                                                                .toColorInt()
                                                                                ),
                                                                        shape = CircleShape
                                                                ),
                                                contentAlignment = Alignment.Center
                                        ) {
                                                // Could use subscription.icon here if available
                                                Icon(
                                                        imageVector =
                                                                getCategoryIcon(
                                                                        subscription.category
                                                                ),
                                                        contentDescription = subscription.name,
                                                        tint = Color.White,
                                                        modifier = Modifier.size(24.dp)
                                                )
                                        }

                                        Spacer(modifier = Modifier.width(12.dp))

                                        Column {
                                                Text(
                                                        text = subscription.name,
                                                        style =
                                                                MaterialTheme.typography
                                                                        .titleMedium,
                                                        fontWeight = FontWeight.Bold,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis
                                                )

                                                Text(
                                                        text = subscription.category.persianName,
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color =
                                                                MaterialTheme.colorScheme
                                                                        .onSurfaceVariant
                                                )
                                        }
                                }

                                // Menu button
                                Box {
                                        IconButton(onClick = { expanded = true }) {
                                                Icon(
                                                        imageVector = Icons.Default.MoreVert,
                                                        contentDescription = "Menu"
                                                )
                                        }

                                        DropdownMenu(
                                                expanded = expanded,
                                                onDismissRequest = { expanded = false }
                                        ) {
                                                DropdownMenuItem(
                                                        text = {
                                                                Text(
                                                                        if (subscription.isActive)
                                                                                "غیرفعال کردن"
                                                                        else "فعال کردن"
                                                                )
                                                        },
                                                        onClick = {
                                                                expanded = false
                                                                onToggleStatus()
                                                        }
                                                )
                                                DropdownMenuItem(
                                                        text = { Text("ویرایش") },
                                                        onClick = {
                                                                expanded = false
                                                                onEdit()
                                                        }
                                                )
                                                DropdownMenuItem(
                                                        text = { Text("حذف") },
                                                        onClick = {
                                                                expanded = false
                                                                onDelete()
                                                        }
                                                )
                                                DropdownMenuItem(
                                                        text = { Text("پرداخت کردم ✅") },
                                                        onClick = {
                                                                HapticHelper.performConfirm(view)
                                                                isAnimating = true
                                                                expanded = false
                                                                onPaymentConfirm()
                                                        }
                                                )
                                        }
                                }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Price and renewal info
                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                                Column {
                                        Text(
                                                text =
                                                        CurrencyConverter.formatPrice(
                                                                subscription.price,
                                                                subscription.currency
                                                        ),
                                                style = MaterialTheme.typography.titleLarge,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.primary
                                        )

                                        Text(
                                                text = subscription.billingCycle.persianName,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                                text = "بعدی تمدید:",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )

                                        Text(
                                                text = formatDate(subscription.nextRenewalDate),
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Medium
                                        )
                                }
                        }

                        // Status indicator
                        Spacer(modifier = Modifier.height(8.dp))

                        // Calculate days until renewal (used in Row and progress bar)
                        val daysUntilRenewal =
                                calculateDaysUntilRenewal(subscription.nextRenewalDate)
                        val renewalColor =
                                when {
                                        daysUntilRenewal <= 3 -> Color(0xFFF44336) // Red
                                        daysUntilRenewal <= 7 -> Color(0xFFFF9800) // Orange
                                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                                }

                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                                // Status indicator
                                val statusColor by
                                        animateColorAsState(
                                                targetValue =
                                                        if (subscription.isActive)
                                                                Color(0xFF4CAF50) // Green
                                                        else Color(0xFF9E9E9E) // Gray
                                        )

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                                modifier =
                                                        Modifier.size(8.dp)
                                                                .clip(CircleShape)
                                                                .background(statusColor)
                                        )

                                        Spacer(modifier = Modifier.width(6.dp))

                                        Text(
                                                text =
                                                        if (subscription.isActive) "فعال"
                                                        else "غیرفعال",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = statusColor
                                        )
                                }

                                Text(
                                        text = "$daysUntilRenewal روز دیگر",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = renewalColor,
                                        fontWeight =
                                                if (daysUntilRenewal <= 3) FontWeight.Bold
                                                else FontWeight.Normal
                                )
                        }

                        // Progress bar for remaining time
                        val totalDays = subscription.billingCycle.days
                        val usedDays = (totalDays - daysUntilRenewal).coerceAtLeast(0)
                        val progress = if (totalDays > 0) usedDays.toFloat() / totalDays else 0f
                        val progressColor =
                                when {
                                        progress >= 0.9f -> Color(0xFFF44336) // Red
                                        progress >= 0.7f -> Color(0xFFFF9800) // Orange
                                        else -> MaterialTheme.colorScheme.primary
                                }

                        Spacer(modifier = Modifier.height(8.dp))

                        LinearProgressIndicator(
                                progress = { progress.coerceIn(0f, 1f) },
                                modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                                color = progressColor,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                }
        }
}

@Composable
private fun getCategoryIcon(category: SubscriptionCategory): ImageVector {
        return when (category) {
                SubscriptionCategory.ENTERTAINMENT -> Icons.Default.Edit
                SubscriptionCategory.PRODUCTIVITY -> Icons.Default.Edit
                SubscriptionCategory.EDUCATION -> Icons.Default.Edit
                SubscriptionCategory.HEALTH -> Icons.Default.Edit
                SubscriptionCategory.NEWS -> Icons.Default.Edit
                SubscriptionCategory.MUSIC -> Icons.Default.Edit
                SubscriptionCategory.VIDEO -> Icons.Default.Edit
                SubscriptionCategory.CLOUD -> Icons.Default.Edit
                SubscriptionCategory.OTHER -> Icons.Default.Edit
        }
}

// formatPrice removed - now using CurrencyConverter.formatPrice

private fun formatDate(date: Date): String {
        val localDate = date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate()
        val jalaliDate = ir.dekot.eshterakyar.core.domain.utils.DateConverter.toJalali(localDate)
        return "${jalaliDate.day} ${jalaliDate.monthName()} ${jalaliDate.year}"
}

private fun calculateDaysUntilRenewal(date: Date): Int {
        val currentDate = Date()
        val diffInMillis = date.time - currentDate.time
        return (diffInMillis / (1000 * 60 * 60 * 24)).toInt()
}
