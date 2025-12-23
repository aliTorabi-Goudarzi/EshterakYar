package ir.dekot.eshterakyar.feature_addSubscription.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ir.dekot.eshterakyar.R
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.BillingCycle
import ir.dekot.eshterakyar.feature_addSubscription.presentation.state.AddSubscriptionUiState

/**
 * مرحله دوم: اطلاعات پرداخت (مبلغ، ارز، دوره تمدید)
 * Step 2: Payment Details (Price, Currency, Billing Cycle)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentStep(
    uiState: AddSubscriptionUiState,
    onPriceChange: (String) -> Unit,
    onCurrencyChange: (String) -> Unit,
    onBillingCycleChange: (BillingCycle) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ردیف مبلغ و ارز
        // Price and Currency Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // فیلد مبلغ
            // Price Field
            OutlinedTextField(
                value = uiState.price,
                onValueChange = onPriceChange,
                label = { Text(stringResource(R.string.price_label)) },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                isError = uiState.priceError != null,
                supportingText = uiState.priceError?.let { { Text(it) } },
                singleLine = true
            )

            // منوی کشویی ارز
            // Currency Dropdown
            var currencyExpanded by remember { mutableStateOf(false) }
            val currencies = listOf("IRT", "USD")

            ExposedDropdownMenuBox(
                expanded = currencyExpanded,
                onExpandedChange = { currencyExpanded = !currencyExpanded },
                modifier = Modifier.weight(0.6f)
            ) {
                OutlinedTextField(
                    value = when (uiState.currency) {
                        "IRT" -> stringResource(R.string.toman)
                        "USD" -> stringResource(R.string.dollar)
                        else -> uiState.currency
                    },
                    onValueChange = { },
                    readOnly = true,
                    label = { Text(stringResource(R.string.currency_label)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = currencyExpanded) },
                    modifier = Modifier.menuAnchor(),
                )

                ExposedDropdownMenu(
                    expanded = currencyExpanded,
                    onDismissRequest = { currencyExpanded = false }
                ) {
                    currencies.forEach { currency ->
                        DropdownMenuItem(
                            text = { 
                                Text(when (currency) {
                                    "IRT" -> stringResource(R.string.toman)
                                    "USD" -> stringResource(R.string.dollar)
                                    else -> currency
                                }) 
                            },
                            onClick = {
                                onCurrencyChange(currency)
                                currencyExpanded = false
                            }
                        )
                    }
                }
            }
        }

        // منوی کشویی دوره تمدید
        // Billing Cycle Dropdown
        var cycleExpanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = cycleExpanded,
            onExpandedChange = { cycleExpanded = !cycleExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = uiState.billingCycle.persianName,
                onValueChange = { },
                readOnly = true,
                label = { Text(stringResource(R.string.billing_cycle_label)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cycleExpanded) },
                modifier = Modifier.menuAnchor(),
            )

            ExposedDropdownMenu(
                expanded = cycleExpanded,
                onDismissRequest = { cycleExpanded = false }
            ) {
                BillingCycle.entries.forEach { cycle ->
                    DropdownMenuItem(
                        text = { Text(cycle.persianName) },
                        onClick = {
                            onBillingCycleChange(cycle)
                            cycleExpanded = false
                        }
                    )
                }
            }
        }
    }
}