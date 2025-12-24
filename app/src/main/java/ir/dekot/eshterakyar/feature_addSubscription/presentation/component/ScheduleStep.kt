package ir.dekot.eshterakyar.feature_addSubscription.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ir.dekot.eshterakyar.R
import ir.dekot.eshterakyar.feature_addSubscription.presentation.state.AddSubscriptionUiState
import java.util.Date

/**
 * مرحله سوم: زمان‌بندی (تاریخ تمدید و وضعیت فعال بودن) Step 3: Schedule (Renewal Date and Active
 * Status)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleStep(
        uiState: AddSubscriptionUiState,
        onDateChange: (Date) -> Unit,
        onActiveChange: (Boolean) -> Unit,
        modifier: Modifier = Modifier
) {
        var showDatePicker by remember { mutableStateOf(false) }
        // استفاده از فرمت تاریخ شمسی (در آینده بهتر است از تقویم شمسی واقعی استفاده شود)
        // Using Persian date format (better to use real Persian Calendar in future)

        if (showDatePicker) {
                val initialJalaliDate =
                        remember(uiState.nextRenewalDate) {
                                val localDate =
                                        uiState.nextRenewalDate
                                                .toInstant()
                                                .atZone(java.time.ZoneId.systemDefault())
                                                .toLocalDate()
                                ir.dekot.eshterakyar.core.domain.utils.DateConverter.toJalali(
                                        localDate
                                )
                        }

                ir.dekot.eshterakyar.core.presentation.components.JalaliDatePickerDialog(
                        onDismissRequest = { showDatePicker = false },
                        onDateChange = { jalaliDate ->
                                val localDate =
                                        ir.dekot.eshterakyar.core.domain.utils.DateConverter
                                                .toGregorian(jalaliDate)
                                val date =
                                        java.util.Date.from(
                                                localDate
                                                        .atStartOfDay(
                                                                java.time.ZoneId.systemDefault()
                                                        )
                                                        .toInstant()
                                        )
                                onDateChange(date)
                                showDatePicker = false
                        },
                        initialDate = initialJalaliDate
                )
        }

        Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
                // کارت انتخاب تاریخ
                // Date Selector Card
                Card(
                        modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true },
                        colors =
                                CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                                ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                                Text(
                                        text = stringResource(R.string.next_renewal_date_label),
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                        text =
                                                with(uiState.nextRenewalDate) {
                                                        val localDate =
                                                                this.toInstant()
                                                                        .atZone(
                                                                                java.time.ZoneId
                                                                                        .systemDefault()
                                                                        )
                                                                        .toLocalDate()
                                                        val jalaliDate =
                                                                ir.dekot.eshterakyar.core.domain
                                                                        .utils.DateConverter
                                                                        .toJalali(localDate)
                                                        "${jalaliDate.day} ${jalaliDate.monthName()} ${jalaliDate.year}"
                                                },
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                        text = stringResource(R.string.change_date_hint),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                        }
                }

                // کارت وضعیت فعال بودن
                // Active Status Card
                Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors =
                                CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                                ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                        Row(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                                Column {
                                        Text(
                                                text =
                                                        stringResource(
                                                                R.string.subscription_status_label
                                                        ),
                                                style = MaterialTheme.typography.bodyLarge,
                                                fontWeight = FontWeight.Medium,
                                                color = MaterialTheme.colorScheme.onSurface
                                        )

                                        Text(
                                                text =
                                                        if (uiState.isActive)
                                                                stringResource(R.string.active)
                                                        else stringResource(R.string.inactive),
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                }

                                Switch(checked = uiState.isActive, onCheckedChange = onActiveChange)
                        }
                }
        }
}
