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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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

@Composable
fun CompactStatsCard(
    totalBudget: Double,
    currentSpent: Double,
    activeCount: Int,
    inactiveCount: Int,
    nearingRenewalCount: Int,
    modifier: Modifier = Modifier
) {
    val theme = LocalTheme.current
    var animatedProgress by remember { mutableFloatStateOf(0f) }
    
    LaunchedEffect(currentSpent) {
        animatedProgress = 1f
    }
    
    val progressAnimation by animateFloatAsState(
        targetValue = animatedProgress,
        animationSpec = tween(durationMillis = 1000),
        label = "progress"
    )
    
    val budgetUsagePercentage = (currentSpent / totalBudget).coerceAtMost(1.0)
    
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = theme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circular progress indicator
            Box(
                modifier = Modifier.size(80.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { progressAnimation * budgetUsagePercentage.toFloat() },
                    modifier = Modifier.size(70.dp),
                    color = if (currentSpent > totalBudget) Color.Red else theme.primary,
                    strokeWidth = 8.dp,
                    trackColor = theme.onPrimaryContainer.copy(alpha = 0.2f)
                )
                
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${(budgetUsagePercentage * 100).toInt()}%",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = theme.onPrimaryContainer,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "بودجه",
                        style = MaterialTheme.typography.labelSmall,
                        color = theme.onPrimaryContainer.copy(alpha = 0.8f),
                        fontSize = 10.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Stats columns
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Total budget
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "بودجه کل:",
                        style = MaterialTheme.typography.bodySmall,
                        color = theme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                    Text(
                        text = formatPrice(totalBudget),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = theme.onPrimaryContainer
                    )
                }
                
                // Three metrics
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CompactStatItem(
                        title = "فعال",
                        value = activeCount.toString(),
                        color = theme.primary
                    )
                    
                    CompactStatItem(
                        title = "غیرفعال",
                        value = inactiveCount.toString(),
                        color = theme.secondary
                    )
                    
                    CompactStatItem(
                        title = "نزدیک تمدید",
                        value = nearingRenewalCount.toString(),
                        color = Color(0xFFFF9800) // Orange color for warning
                    )
                }
            }
        }
    }
}

@Composable
private fun CompactStatItem(
    title: String,
    value: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
        
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = color.copy(alpha = 0.8f)
        )
    }
}

private fun formatPrice(price: Double): String {
    return "${price.toInt()} تومان"
}