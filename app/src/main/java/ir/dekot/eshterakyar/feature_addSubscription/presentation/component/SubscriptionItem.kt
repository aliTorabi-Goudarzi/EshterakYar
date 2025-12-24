package ir.dekot.eshterakyar.feature_addSubscription.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.Subscription
import java.text.NumberFormat
import java.util.Locale

@Composable
fun SubscriptionItem(
        subscription: Subscription,
        onClick: () -> Unit,
        modifier: Modifier = Modifier
) {
    Card(
            modifier = modifier.fillMaxWidth().clickable(onClick = onClick),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            val color =
                    try {
                        Color(subscription.colorCode.toColorInt())
                    } catch (e: Exception) {
                        MaterialTheme.colorScheme.primary
                    }

            Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(color),
                    contentAlignment = Alignment.Center
            ) {
                Text(
                        text = subscription.name.take(1).uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Name and Category
            Column(modifier = Modifier.weight(1f)) {
                Text(
                        text = subscription.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                )
                Text(
                        text = subscription.category.persianName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Price and Date
            Column(horizontalAlignment = Alignment.End) {
                Text(
                        text = formatPrice(subscription.price, subscription.currency),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                        text = formatDate(subscription.nextRenewalDate),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun formatPrice(price: Double, currency: String): String {
    val formatter = NumberFormat.getInstance(Locale("fa", "IR"))
    return "${formatter.format(price)} $currency"
}

private fun formatDate(date: java.util.Date): String {
    val localDate = date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate()
    val jalaliDate = ir.dekot.eshterakyar.core.domain.utils.DateConverter.toJalali(localDate)
    return "${jalaliDate.day} ${jalaliDate.monthName()}"
}
