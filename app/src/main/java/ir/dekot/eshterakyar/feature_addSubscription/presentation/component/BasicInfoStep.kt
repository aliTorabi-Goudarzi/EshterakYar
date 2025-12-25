package ir.dekot.eshterakyar.feature_addSubscription.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.dp
import ir.dekot.eshterakyar.R
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.SubscriptionCategory
import ir.dekot.eshterakyar.feature_addSubscription.presentation.state.AddSubscriptionUiState

/** مرحله اول: اطلاعات پایه (نام و دسته‌بندی) Step 1: Basic Information (Name and Category) */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicInfoStep(
        uiState: AddSubscriptionUiState,
        onNameChange: (String) -> Unit,
        onCategoryChange: (SubscriptionCategory) -> Unit,
        onPresetSelected:
                (ir.dekot.eshterakyar.feature_addSubscription.domain.model.ServicePreset) -> Unit =
                {},
        modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // انتخابگر پریست‌های سرویس‌های محبوب
        // Service Preset Selector
        if (uiState.servicePresets.isNotEmpty()) {
            ServicePresetSelector(
                    presets = uiState.servicePresets,
                    selectedPreset = uiState.selectedPreset,
                    onPresetSelected = onPresetSelected
            )
        }
        // فیلد نام اشتراک
        // Subscription Name Field
        OutlinedTextField(
                value = uiState.name,
                onValueChange = onNameChange,
                label = { Text(stringResource(R.string.subscription_name)) },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.nameError != null,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                supportingText = uiState.nameError?.let { { Text(it) } },
                singleLine = true
        )

        // منوی کشویی انتخاب دسته‌بندی
        // Category Selection Dropdown
        var expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                    value = uiState.category.persianName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.category_label)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier.menuAnchor(),
            )

            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                SubscriptionCategory.entries.forEach { category ->
                    DropdownMenuItem(
                            text = { Text(category.persianName) },
                            onClick = {
                                onCategoryChange(category)
                                expanded = false
                            }
                    )
                }
            }
        }
    }
}
