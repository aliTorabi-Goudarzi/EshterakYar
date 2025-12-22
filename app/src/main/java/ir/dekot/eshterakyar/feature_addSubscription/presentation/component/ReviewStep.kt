package ir.dekot.eshterakyar.feature_addSubscription.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ir.dekot.eshterakyar.R
import ir.dekot.eshterakyar.feature_addSubscription.presentation.state.AddSubscriptionUiState
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * مرحله چهارم: بررسی نهایی و ذخیره
 * Step 4: Final Review and Save
 */
@Composable
fun ReviewStep(
    uiState: AddSubscriptionUiState,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formatter = SimpleDateFormat("dd MMM yyyy", Locale("fa", "IR"))

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // عنوان بخش بررسی
        // Review section title
        Text(
            text = stringResource(R.string.review_info),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        // کارت خلاصه اطلاعات
        // Info Summary Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ReviewItem(
                    label = stringResource(R.string.subscription_name),
                    value = uiState.name
                )
                
                HorizontalDivider(color = MaterialTheme.colorScheme.surface)

                ReviewItem(
                    label = stringResource(R.string.category_label),
                    value = uiState.category.persianName
                )
                
                HorizontalDivider(color = MaterialTheme.colorScheme.surface)

                ReviewItem(
                    label = stringResource(R.string.price_label),
                    value = "${uiState.price} ${if (uiState.currency == "IRT") stringResource(R.string.toman) else stringResource(R.string.dollar)}"
                )
                
                HorizontalDivider(color = MaterialTheme.colorScheme.surface)

                ReviewItem(
                    label = stringResource(R.string.billing_cycle_label),
                    value = uiState.billingCycle.persianName
                )
                
                HorizontalDivider(color = MaterialTheme.colorScheme.surface)

                ReviewItem(
                    label = stringResource(R.string.next_renewal_date_label),
                    value = formatter.format(uiState.nextRenewalDate)
                )
                
                HorizontalDivider(color = MaterialTheme.colorScheme.surface)

                ReviewItem(
                    label = stringResource(R.string.subscription_status_label),
                    value = if (uiState.isActive) stringResource(R.string.active) else stringResource(R.string.inactive)
                )
            }
        }

        // دکمه تایید و ذخیره
        // Confirm and Save Button
        Button(
            onClick = onConfirm,
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isSaving
        ) {
            if (uiState.isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(20.dp)
                        .padding(end = 8.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            }
            Text(stringResource(R.string.confirm_and_save))
        }
    }
}

@Composable
private fun ReviewItem(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
