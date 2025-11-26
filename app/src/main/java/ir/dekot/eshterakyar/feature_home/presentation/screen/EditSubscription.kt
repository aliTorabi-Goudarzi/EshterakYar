package ir.dekot.eshterakyar.feature_home.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import ir.dekot.eshterakyar.core.utils.LocalTheme
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.BillingCycle
import ir.dekot.eshterakyar.feature_home.presentation.viewmodel.EditSubscriptionViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditSubscriptionScreen(
    subscriptionId: Long,
    viewModel: EditSubscriptionViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val theme = LocalTheme.current

    LaunchedEffect(subscriptionId) {
        viewModel.loadSubscription(subscriptionId)
    }

    // Handle successful save
    LaunchedEffect(uiState.isSaving) {
        if (!uiState.isSaving && uiState.error == null && !uiState.isLoading) {
            // If we're not saving, not loading, and no error, assume save was successful
            if (uiState.subscription != null) {
                viewModel.goBack()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ویرایش اشتراک") },
                navigationIcon = {
                    IconButton(onClick = {viewModel.goBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "بازگشت")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.saveSubscription()
                        },
                        enabled = !uiState.isSaving
                    ) {
                        if (uiState.isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.padding(12.dp),
                                strokeWidth = 2.dp,
                                color = theme.onSurface
                            )
                        } else {
                            Icon(Icons.Default.Check, contentDescription = "ذخیره")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = theme.surface,
                    titleContentColor = theme.onSurface,
                    navigationIconContentColor = theme.onSurface,
                    actionIconContentColor = theme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = theme.primary)
                    }
                }

                uiState.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "خطا",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = uiState.error?:"",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }

                else -> {
                    EditSubscriptionContent(
                        uiState = uiState,
                        onNameChange = viewModel::updateName,
                        onPriceChange = viewModel::updatePrice,
                        onCurrencyChange = viewModel::updateCurrency,
                        onBillingCycleChange = viewModel::updateBillingCycle,
                        onNextRenewalDateChange = viewModel::updateNextRenewalDate,
                        onIsActiveChange = viewModel::updateIsActive,
                        onSave = viewModel::saveSubscription,
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                            .imePadding()
                    )
                }
            }
        }
    }
}

@Composable
private fun EditSubscriptionContent(
    uiState: ir.dekot.eshterakyar.feature_home.presentation.state.EditSubscriptionUiState,
    onNameChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onCurrencyChange: (String) -> Unit,
    onBillingCycleChange: (BillingCycle) -> Unit,
    onNextRenewalDateChange: (Date) -> Unit,
    onIsActiveChange: (Boolean) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalTheme.current
    
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Name field
        OutlinedTextField(
            value = uiState.name,
            onValueChange = onNameChange,
            label = { Text("نام اشتراک") },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.nameError != null,
            supportingText = uiState.nameError?.let { { Text(it) } }
        )
        
        // Price and Currency row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = uiState.price,
                onValueChange = onPriceChange,
                label = { Text("مبلغ") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = uiState.priceError != null,
                supportingText = uiState.priceError?.let { { Text(it) } }
            )
            
            CurrencyDropdown(
                selectedCurrency = uiState.currency,
                onCurrencyChange = onCurrencyChange,
                modifier = Modifier.weight(0.6f)
            )
        }
        
        // Billing Cycle dropdown
        BillingCycleDropdown(
            selectedCycle = uiState.billingCycle,
            onCycleChange = onBillingCycleChange,
            modifier = Modifier.fillMaxWidth()
        )
        
        // Next Renewal Date
        DateSelectorCard(
            selectedDate = uiState.nextRenewalDate,
            onDateChange = onNextRenewalDateChange,
            modifier = Modifier.fillMaxWidth()
        )
        
        // Active Status
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = theme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "وضعیت اشتراک",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = theme.onSurface
                    )
                    
                    Text(
                        text = if (uiState.isActive) "فعال" else "غیرفعال",
                        style = MaterialTheme.typography.bodyMedium,
                        color = theme.onSurfaceVariant
                    )
                }
                
                Switch(
                    checked = uiState.isActive,
                    onCheckedChange = onIsActiveChange
                )
            }
        }
        
        // Save button
        Button(
            onClick = onSave,
            enabled = !uiState.isSaving,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState.isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(end = 8.dp),
                    strokeWidth = 2.dp,
                    color = theme.onPrimary
                )
            }
            Text("ذخیره تغییرات")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CurrencyDropdown(
    selectedCurrency: String,
    onCurrencyChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val currencies = listOf("IRT", "USD")
    var expanded by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = when (selectedCurrency) {
                "IRT" -> "تومان"
                "USD" -> "دلار"
                else -> selectedCurrency
            },
            onValueChange = { },
            readOnly = true,
            label = { Text("واحد پول") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor()
        )
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            currencies.forEach { currency ->
                DropdownMenuItem(
                    text = { Text(when (currency) {
                        "IRT" -> "تومان"
                        "USD" -> "دلار"
                        else -> currency
                    }) },
                    onClick = {
                        onCurrencyChange(currency)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BillingCycleDropdown(
    selectedCycle: BillingCycle,
    onCycleChange: (BillingCycle) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedCycle.persianName,
            onValueChange = { },
            readOnly = true,
            label = { Text("دوره تمدید") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor()
        )
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            BillingCycle.entries.forEach { cycle ->
                DropdownMenuItem(
                    text = { Text(cycle.persianName) },
                    onClick = {
                        onCycleChange(cycle)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun DateSelectorCard(
    selectedDate: Date,
    onDateChange: (Date) -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalTheme.current
    val formatter = SimpleDateFormat("dd MMM yyyy", Locale("fa", "IR"))
    
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = theme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "تاریخ تمدید بعدی",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = theme.onSurface
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = formatter.format(selectedDate),
                style = MaterialTheme.typography.bodyMedium,
                color = theme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // For now, just display the date. In a real implementation, 
            // you would add a date picker here
            Text(
                text = "برای تغییر تاریخ، از تقویم استفاده کنید",
                style = MaterialTheme.typography.bodySmall,
                color = theme.onSurfaceVariant
            )
        }
    }
}
