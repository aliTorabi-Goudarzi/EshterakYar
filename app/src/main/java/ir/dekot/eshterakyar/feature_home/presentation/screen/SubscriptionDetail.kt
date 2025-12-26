package ir.dekot.eshterakyar.feature_home.presentation.screen

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.dekot.eshterakyar.core.utils.LocalTheme
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.Subscription
import ir.dekot.eshterakyar.feature_home.presentation.viewmodel.SubscriptionDetailViewModel
import ir.dekot.eshterakyar.feature_payment.data.model.PaymentLog
import java.util.Date
import org.koin.androidx.compose.koinViewModel
import sv.lib.squircleshape.SquircleShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionDetailScreen(
        subscriptionId: Long,
        viewModel: SubscriptionDetailViewModel = koinViewModel()
) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val theme = LocalTheme.current

        LaunchedEffect(subscriptionId) { viewModel.loadSubscription(subscriptionId) }

        Scaffold(
                topBar = {
                        TopAppBar(
                                title = { Text("جزئیات اشتراک") },
                                navigationIcon = {
                                        IconButton(onClick = { viewModel.goBack() }) {
                                                Icon(
                                                        Icons.AutoMirrored.Filled.ArrowBack,
                                                        contentDescription = "بازگشت"
                                                )
                                        }
                                },
                                colors =
                                        TopAppBarDefaults.topAppBarColors(
                                                containerColor = theme.surface,
                                                titleContentColor = theme.onSurface,
                                                navigationIconContentColor = theme.onSurface
                                        )
                        )
                },
                floatingActionButton = {
                        if (uiState.subscription != null) {
                                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                        FloatingActionButton(
                                                onClick = {
                                                        viewModel.navigateToEditSubscription(
                                                                subscriptionId
                                                        )
                                                },
                                                containerColor = theme.primary
                                        ) {
                                                Icon(
                                                        Icons.Default.Edit,
                                                        contentDescription = "ویرایش"
                                                )
                                        }

                                        FloatingActionButton(
                                                onClick = {
                                                        viewModel.deleteSubscription(
                                                                uiState.subscription!!
                                                        )
                                                        viewModel.goBack()
                                                },
                                                containerColor = Color.Red
                                        ) { Icon(Icons.Default.Delete, contentDescription = "حذف") }
                                }
                        }
                }
        ) { paddingValues ->
                Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                        when {
                                uiState.isLoading -> {
                                        Box(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                        ) { CircularProgressIndicator(color = theme.primary) }
                                }
                                uiState.error != null -> {
                                        Box(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                        ) {
                                                Column(
                                                        horizontalAlignment =
                                                                Alignment.CenterHorizontally
                                                ) {
                                                        Text(
                                                                text = "خطا در بارگذاری اطلاعات",
                                                                style =
                                                                        MaterialTheme.typography
                                                                                .titleMedium,
                                                                color =
                                                                        MaterialTheme.colorScheme
                                                                                .error
                                                        )

                                                        Spacer(modifier = Modifier.height(8.dp))

                                                        Text(
                                                                text = uiState.error ?: "",
                                                                style =
                                                                        MaterialTheme.typography
                                                                                .bodyMedium,
                                                                color =
                                                                        MaterialTheme.colorScheme
                                                                                .onErrorContainer
                                                        )
                                                }
                                        }
                                }
                                uiState.subscription != null -> {
                                        SubscriptionDetailContent(
                                                subscription = uiState.subscription!!,
                                                paymentLogs = uiState.paymentLogs,
                                                modifier =
                                                        Modifier.fillMaxSize()
                                                                .verticalScroll(
                                                                        rememberScrollState()
                                                                )
                                                                .padding(16.dp)
                                        )
                                }
                        }
                }
        }
}

@Composable
private fun SubscriptionDetailContent(
        subscription: Subscription,
        paymentLogs: List<PaymentLog> = emptyList(),
        modifier: Modifier = Modifier
) {
        val theme = LocalTheme.current

        Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Main info card
                Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = theme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        shape =
                                SquircleShape(
                                        radius = 20.dp,
                                        smoothing = sv.lib.squircleshape.CornerSmoothing.Medium
                                )
                ) {
                        Column(
                                modifier = Modifier.fillMaxWidth().padding(20.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                                // Header with icon and name
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                                modifier =
                                                        Modifier.size(64.dp)
                                                                .clip(CircleShape)
                                                                .background(
                                                                        Color(
                                                                                subscription
                                                                                        .colorCode
                                                                                        .toColorInt()
                                                                        )
                                                                ),
                                                contentAlignment = Alignment.Center
                                        ) {
                                                Icon(
                                                        imageVector =
                                                                getCategoryIcon(
                                                                        subscription.category
                                                                ),
                                                        contentDescription = subscription.name,
                                                        tint = Color.White,
                                                        modifier = Modifier.size(32.dp)
                                                )
                                        }

                                        Spacer(modifier = Modifier.width(16.dp))

                                        Column {
                                                Text(
                                                        text = subscription.name,
                                                        style =
                                                                MaterialTheme.typography
                                                                        .headlineSmall,
                                                        fontWeight = FontWeight.Bold,
                                                        color = theme.onSurface
                                                )

                                                Text(
                                                        text = subscription.category.persianName,
                                                        style = MaterialTheme.typography.bodyLarge,
                                                        color = theme.onSurfaceVariant
                                                )
                                        }
                                }

                                // Price and billing cycle
                                Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                ) {
                                        Column {
                                                Text(
                                                        text =
                                                                formatPrice(
                                                                        subscription.price,
                                                                        subscription.currency
                                                                ),
                                                        style =
                                                                MaterialTheme.typography
                                                                        .headlineMedium,
                                                        fontWeight = FontWeight.Bold,
                                                        color = theme.primary
                                                )

                                                Text(
                                                        text =
                                                                subscription
                                                                        .billingCycle
                                                                        .persianName,
                                                        style = MaterialTheme.typography.bodyLarge,
                                                        color = theme.onSurfaceVariant
                                                )
                                        }

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
                                                                Modifier.size(12.dp)
                                                                        .clip(CircleShape)
                                                                        .background(statusColor)
                                                )

                                                Spacer(modifier = Modifier.width(8.dp))

                                                Text(
                                                        text =
                                                                if (subscription.isActive) "فعال"
                                                                else "غیرفعال",
                                                        style = MaterialTheme.typography.bodyLarge,
                                                        color = statusColor,
                                                        fontWeight = FontWeight.Medium
                                                )
                                        }
                                }
                        }
                }

                // Renewal info card
                Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = theme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        shape =
                                SquircleShape(
                                        radius = 20.dp,
                                        smoothing = sv.lib.squircleshape.CornerSmoothing.Medium
                                )
                ) {
                        Column(
                                modifier = Modifier.fillMaxWidth().padding(20.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                                Text(
                                        text = "اطلاعات تمدید",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = theme.onSurface
                                )

                                Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                        Column {
                                                Text(
                                                        text = "تاریخ بعدی تمدید",
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        color = theme.onSurfaceVariant
                                                )

                                                Text(
                                                        text =
                                                                formatDate(
                                                                        subscription.nextRenewalDate
                                                                ),
                                                        style = MaterialTheme.typography.bodyLarge,
                                                        fontWeight = FontWeight.Medium,
                                                        color = theme.onSurface
                                                )
                                        }

                                        Column(horizontalAlignment = Alignment.End) {
                                                Text(
                                                        text = "زمان باقی‌مانده",
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        color = theme.onSurfaceVariant
                                                )

                                                val daysUntilRenewal =
                                                        calculateDaysUntilRenewal(
                                                                subscription.nextRenewalDate
                                                        )
                                                val renewalColor =
                                                        when {
                                                                daysUntilRenewal <= 3 ->
                                                                        Color(0xFFF44336) // Red
                                                                daysUntilRenewal <= 7 ->
                                                                        Color(0xFFFF9800) // Orange
                                                                else -> theme.onSurface
                                                        }

                                                Text(
                                                        text = "$daysUntilRenewal روز",
                                                        style = MaterialTheme.typography.bodyLarge,
                                                        fontWeight =
                                                                if (daysUntilRenewal <= 3)
                                                                        FontWeight.Bold
                                                                else FontWeight.Medium,
                                                        color = renewalColor
                                                )
                                        }
                                }
                        }
                }

                // Description card (if available)
                if (subscription.description.isNotBlank()) {
                        val clipboardManager = LocalClipboardManager.current
                        val context = LocalContext.current

                        Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = theme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                shape =
                                        SquircleShape(
                                                radius = 20.dp,
                                                smoothing =
                                                        sv.lib.squircleshape.CornerSmoothing.Medium
                                        )
                        ) {
                                Column(
                                        modifier = Modifier.fillMaxWidth().padding(20.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                        Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                        ) {
                                                Text(
                                                        text = "توضیحات",
                                                        style =
                                                                MaterialTheme.typography
                                                                        .titleMedium,
                                                        fontWeight = FontWeight.Bold,
                                                        color = theme.onSurface
                                                )

                                                IconButton(
                                                        onClick = {
                                                                clipboardManager.setText(
                                                                        AnnotatedString(
                                                                                subscription
                                                                                        .description
                                                                        )
                                                                )
                                                                Toast.makeText(
                                                                                context,
                                                                                "کپی شد!",
                                                                                Toast.LENGTH_SHORT
                                                                        )
                                                                        .show()
                                                        },
                                                        modifier = Modifier.size(32.dp)
                                                ) {
                                                        Icon(
                                                                imageVector =
                                                                        Icons.Default.ContentCopy,
                                                                contentDescription = "کپی",
                                                                tint = theme.primary,
                                                                modifier = Modifier.size(20.dp)
                                                        )
                                                }
                                        }

                                        Text(
                                                text = subscription.description,
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = theme.onSurface
                                        )
                                }
                        }
                }

                // Payment History card
                if (paymentLogs.isNotEmpty()) {
                        Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = theme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                shape =
                                        SquircleShape(
                                                radius = 20.dp,
                                                smoothing =
                                                        sv.lib.squircleshape.CornerSmoothing.Medium
                                        )
                        ) {
                                Column(
                                        modifier = Modifier.fillMaxWidth().padding(20.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                        Text(
                                                text = "تاریخچه پرداخت‌ها",
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = theme.onSurface
                                        )

                                        paymentLogs.forEach { log ->
                                                Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement =
                                                                Arrangement.SpaceBetween,
                                                        verticalAlignment =
                                                                Alignment.CenterVertically
                                                ) {
                                                        Column {
                                                                Text(
                                                                        text =
                                                                                formatDate(
                                                                                        log.paymentDate
                                                                                ),
                                                                        style =
                                                                                MaterialTheme
                                                                                        .typography
                                                                                        .bodyMedium,
                                                                        color = theme.onSurface
                                                                )
                                                                if (log.note.isNotBlank()) {
                                                                        Text(
                                                                                text = log.note,
                                                                                style =
                                                                                        MaterialTheme
                                                                                                .typography
                                                                                                .bodySmall,
                                                                                color =
                                                                                        theme.onSurfaceVariant
                                                                        )
                                                                }
                                                        }
                                                        Text(
                                                                text =
                                                                        "${log.amount.toInt()} تومان",
                                                                style =
                                                                        MaterialTheme.typography
                                                                                .bodyLarge,
                                                                fontWeight = FontWeight.Medium,
                                                                color = theme.primary
                                                        )
                                                }
                                        }
                                }
                        }
                }
        }
}

@Composable
private fun getCategoryIcon(
        category: ir.dekot.eshterakyar.feature_addSubscription.domain.model.SubscriptionCategory
): ImageVector {
        return when (category) {
                ir.dekot
                        .eshterakyar
                        .feature_addSubscription
                        .domain
                        .model
                        .SubscriptionCategory
                        .ENTERTAINMENT -> Icons.Default.Edit
                ir.dekot
                        .eshterakyar
                        .feature_addSubscription
                        .domain
                        .model
                        .SubscriptionCategory
                        .PRODUCTIVITY -> Icons.Default.Edit
                ir.dekot
                        .eshterakyar
                        .feature_addSubscription
                        .domain
                        .model
                        .SubscriptionCategory
                        .EDUCATION -> Icons.Default.Edit
                ir.dekot
                        .eshterakyar
                        .feature_addSubscription
                        .domain
                        .model
                        .SubscriptionCategory
                        .HEALTH -> Icons.Default.Edit
                ir.dekot
                        .eshterakyar
                        .feature_addSubscription
                        .domain
                        .model
                        .SubscriptionCategory
                        .NEWS -> Icons.Default.Edit
                ir.dekot
                        .eshterakyar
                        .feature_addSubscription
                        .domain
                        .model
                        .SubscriptionCategory
                        .MUSIC -> Icons.Default.Edit
                ir.dekot
                        .eshterakyar
                        .feature_addSubscription
                        .domain
                        .model
                        .SubscriptionCategory
                        .VIDEO -> Icons.Default.Edit
                ir.dekot
                        .eshterakyar
                        .feature_addSubscription
                        .domain
                        .model
                        .SubscriptionCategory
                        .CLOUD -> Icons.Default.Edit
                ir.dekot
                        .eshterakyar
                        .feature_addSubscription
                        .domain
                        .model
                        .SubscriptionCategory
                        .OTHER -> Icons.Default.Edit
        }
}

private fun formatPrice(price: Double, currency: String): String {
        return when (currency) {
                "IRT" -> "${price.toInt()} تومان"
                "USD" -> "$${price}"
                else -> "$price $currency"
        }
}

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
