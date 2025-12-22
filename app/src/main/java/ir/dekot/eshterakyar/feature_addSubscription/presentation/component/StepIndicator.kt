package ir.dekot.eshterakyar.feature_addSubscription.presentation.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.dekot.eshterakyar.R

/**
 * نمایشگر مرحله فعلی در فرم چند مرحله‌ای
 * Shows the current step indicator in the multi-step form
 */
@Composable
fun StepIndicator(
    currentStep: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // متن شمارنده مرحله
        // Step counter text
        Text(
            text = stringResource(R.string.step_counter, currentStep, totalSteps),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(8.dp))

        // نوار پیشرفت
        // Progress bars
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(totalSteps) { index ->
                val stepNumber = index + 1
                val isActive = stepNumber <= currentStep
                val isCurrent = stepNumber == currentStep
                
                val weight by animateFloatAsState(
                    targetValue = if (isCurrent) 2f else 1f,
                    label = "weight"
                )
                
                val color by animateColorAsState(
                    targetValue = if (isActive) MaterialTheme.colorScheme.primary 
                                else MaterialTheme.colorScheme.surfaceVariant,
                    label = "color"
                )

                Box(
                    modifier = Modifier
                        .weight(weight)
                        .height(4.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StepIndicatorPreview() {
    MaterialTheme {
        StepIndicator(
            currentStep = 2,
            totalSteps = 4,
            modifier = Modifier.padding(16.dp)
        )
    }
}
