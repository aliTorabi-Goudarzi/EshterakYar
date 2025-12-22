package ir.dekot.eshterakyar.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.dekot.eshterakyar.core.utils.LocalTheme
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.BillingCycle
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.SubscriptionCategory
import ir.dekot.eshterakyar.feature_addSubscription.presentation.viewmodel.AddSubscriptionViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import ir.dekot.eshterakyar.feature_addSubscription.presentation.intent.AddSubscriptionIntent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AddSubscriptionScreen(
    viewModel: AddSubscriptionViewModel = koinViewModel()
) {
    // DEBUG: Log to validate ViewModel instantiation
    Log.d("AddSubscriptionScreen", "DEBUG: AddSubscriptionScreen - Attempting to create ViewModel")
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val theme = LocalTheme.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    
    // Form visibility state
    var isFormExpanded by remember { mutableStateOf(false) }
    var isSubmitting by remember { mutableStateOf(false) }
    
    Log.d("AddSubscriptionScreen", "DEBUG: AddSubscriptionScreen - ViewModel created successfully")

    // Handle successful save with animation
    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            Log.d("AddSubscriptionScreen", "DEBUG: Subscription saved successfully")
            isSubmitting = true
            
            // First collapse the form
            delay(300)
            isFormExpanded = false
            
            // Then show success snackbar
            delay(500)
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = "اشتراک با موفقیت اضافه شد",
                    actionLabel = null,
                    withDismissAction = true
                )
            }
            
            // Reset states
            delay(1000)
            isSubmitting = false
            viewModel.onIntent(AddSubscriptionIntent.OnSuccessDismissed)
        }
    }

    // Handle error with animation
    LaunchedEffect(uiState.error) {
        if (uiState.error != null && isSubmitting) {
            Log.d("AddSubscriptionScreen", "DEBUG: Error during save: ${uiState.error}")
            isSubmitting = false
            
            // Show error snackbar
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = "خطا در افزودن اشتراک: ${uiState.error}",
                    actionLabel = null,
                    withDismissAction = true
                )
            }
            
            // Keep form expanded on error
            viewModel.onIntent(AddSubscriptionIntent.OnErrorDismissed)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Main content area
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (!isFormExpanded && !isSubmitting) {
                    // Initial state - show message
                    Text(
                        text = "برای افزودن اشتراک جدید روی دکمه زیر کلیک کنید",
                        style = MaterialTheme.typography.bodyLarge,
                        color = theme.onSurfaceVariant,
                        modifier = Modifier.padding(16.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Button under the text
                    Button(
                        onClick = {
                            Log.d("AddSubscriptionScreen", "DEBUG: Add button pressed")
                            isFormExpanded = true
                        },
                        enabled = !isSubmitting,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "افزودن اشتراک",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                        Text("افزودن اشتراک")
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Animated form container
                AnimatedVisibility(
                    visible = isFormExpanded,
                    enter = slideInVertically(
                        initialOffsetY = { fullHeight -> -fullHeight / 4 },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    ) + expandVertically(
                        animationSpec = tween(300, easing = androidx.compose.animation.core.EaseOutQuart)
                    ) + fadeIn(animationSpec = tween(300)),
                    exit = slideOutVertically(
                        targetOffsetY = { fullHeight -> -fullHeight / 4 },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    ) + shrinkVertically(
                        animationSpec = tween(300, easing = androidx.compose.animation.core.EaseInQuart)
                    ) + fadeOut(animationSpec = tween(300))
                ) {
                    AddSubscriptionForm(
                        uiState = uiState,
                        onNameChange = { 
                            Log.d("AddSubscriptionScreen", "DEBUG: Name changed to: $it")
                            viewModel.onIntent(AddSubscriptionIntent.OnNameChanged(it))
                        },
                        onPriceChange = { 
                            Log.d("AddSubscriptionScreen", "DEBUG: Price changed to: $it")
                            viewModel.onIntent(AddSubscriptionIntent.OnPriceChanged(it))
                        },
                        onCurrencyChange = { 
                            Log.d("AddSubscriptionScreen", "DEBUG: Currency changed to: $it")
                            viewModel.onIntent(AddSubscriptionIntent.OnCurrencyChanged(it))
                        },
                        onBillingCycleChange = { 
                            Log.d("AddSubscriptionScreen", "DEBUG: Billing cycle changed to: $it")
                            viewModel.onIntent(AddSubscriptionIntent.OnBillingCycleChanged(it))
                        },
                        onNextRenewalDateChange = { 
                            Log.d("AddSubscriptionScreen", "DEBUG: Next renewal date changed to: $it")
                            viewModel.onIntent(AddSubscriptionIntent.OnNextRenewalDateChanged(it))
                        },
                        onIsActiveChange = { 
                            Log.d("AddSubscriptionScreen", "DEBUG: Active status changed to: $it")
                            viewModel.onIntent(AddSubscriptionIntent.OnActiveStatusChanged(it))
                        },
                        onCategoryChange = { 
                            Log.d("AddSubscriptionScreen", "DEBUG: Category changed to: $it")
                            viewModel.onIntent(AddSubscriptionIntent.OnCategoryChanged(it))
                        },
                        onSave = { 
                            Log.d("AddSubscriptionScreen", "DEBUG: Save button in form pressed")
                            isSubmitting = true
                            viewModel.onIntent(AddSubscriptionIntent.OnSaveClicked)
                        },
                        onCancel = {
                            Log.d("AddSubscriptionScreen", "DEBUG: Cancel button pressed")
                            isFormExpanded = false
                        },
                        isSubmitting = isSubmitting,
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                            .imePadding()
                    )
                }
            }
            
            // Loading indicator during submission
            if (isSubmitting && !isFormExpanded) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = theme.primary,
                        strokeWidth = 3.dp
                    )
                }
            }
        }
        
        // Snackbar host at bottom (without navigationBarsPadding to appear on top of bottom bar)
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.padding(bottom = 80.dp) // Add padding to appear above bottom bar
        ) { data ->
            CustomSnackbar(
                message = data.visuals.message,
                isError = uiState.error != null,
                onDismiss = { data.dismiss() }
            )
        }
    }
}

@Composable
private fun AddSubscriptionForm(
    uiState: ir.dekot.eshterakyar.feature_addSubscription.presentation.state.AddSubscriptionUiState,
    onNameChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onCurrencyChange: (String) -> Unit,
    onBillingCycleChange: (BillingCycle) -> Unit,
    onNextRenewalDateChange: (Date) -> Unit,
    onIsActiveChange: (Boolean) -> Unit,
    onCategoryChange: (SubscriptionCategory) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    isSubmitting: Boolean,
    modifier: Modifier = Modifier
) {
    val theme = LocalTheme.current
    
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = theme.surface,
            contentColor = theme.onSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Form header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "افزودن اشتراک جدید",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = theme.onSurface
                )
                
                IconButton(
                    onClick = onCancel,
                    enabled = !isSubmitting
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "بستن",
                        tint = theme.onSurfaceVariant
                    )
                }
            }
            
            // Name field
            OutlinedTextField(
                value = uiState.name,
                onValueChange = onNameChange,
                label = { Text("نام اشتراک") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSubmitting,
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
                    enabled = !isSubmitting,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = uiState.priceError != null,
                    supportingText = uiState.priceError?.let { { Text(it) } }
                )
                
                CurrencyDropdown(
                    selectedCurrency = uiState.currency,
                    onCurrencyChange = onCurrencyChange,
                    modifier = Modifier.weight(0.6f),
                    enabled = !isSubmitting
                )
            }
            
            // Category dropdown
            CategoryDropdown(
                selectedCategory = uiState.category,
                onCategoryChange = onCategoryChange,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSubmitting
            )
            
            // Billing Cycle dropdown
            BillingCycleDropdown(
                selectedCycle = uiState.billingCycle,
                onCycleChange = onBillingCycleChange,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSubmitting
            )
            
            // Next Renewal Date
            DateSelectorCard(
                selectedDate = uiState.nextRenewalDate,
                onDateChange = onNextRenewalDateChange,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSubmitting
            )
            
            // Active Status
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = theme.surfaceVariant),
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
                        onCheckedChange = onIsActiveChange,
                        enabled = !isSubmitting
                    )
                }
            }
            
            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Cancel button
                Button(
                    onClick = onCancel,
                    enabled = !isSubmitting,
                    modifier = Modifier.weight(1f),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = theme.surfaceVariant,
                        contentColor = theme.onSurfaceVariant
                    )
                ) {
                    Text("انصراف")
                }
                
                // Save button
                Button(
                    onClick = onSave,
                    enabled = !isSubmitting && uiState.name.isNotBlank() && uiState.price.isNotBlank(),
                    modifier = Modifier.weight(1f)
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = theme.onPrimary
                        )
                        Spacer(modifier = Modifier.padding(end = 8.dp))
                    }
                    Text("افزودن اشتراک")
                }
            }
        }
    }
}

@Composable
private fun CustomSnackbar(
    message: String,
    isError: Boolean,
    onDismiss: () -> Unit
) {
    val theme = LocalTheme.current
    
    Snackbar(
        modifier = Modifier.padding(16.dp),
        containerColor = if (isError) 
            MaterialTheme.colorScheme.errorContainer 
        else 
            MaterialTheme.colorScheme.primaryContainer,
        contentColor = if (isError) 
            MaterialTheme.colorScheme.onErrorContainer 
        else 
            MaterialTheme.colorScheme.onPrimaryContainer,
        action = {
            IconButton(
                onClick = { onDismiss() },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = if (isError) Icons.Default.Close else Icons.Default.Check,
                    contentDescription = "بستن",
                    tint = if (isError) 
                        MaterialTheme.colorScheme.onErrorContainer 
                    else 
                        MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (isError) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = "خطا",
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CurrencyDropdown(
    selectedCurrency: String,
    onCurrencyChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val currencies = listOf("IRT", "USD")
    var expanded by remember { mutableStateOf(false) }
    val theme = LocalTheme.current
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded && enabled },
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
            modifier = Modifier.menuAnchor(),
            enabled = enabled
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
private fun CategoryDropdown(
    selectedCategory: SubscriptionCategory,
    onCategoryChange: (SubscriptionCategory) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded && enabled },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedCategory.persianName,
            onValueChange = { },
            readOnly = true,
            label = { Text("دسته‌بندی") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(),
            enabled = enabled
        )
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BillingCycleDropdown(
    selectedCycle: BillingCycle,
    onCycleChange: (BillingCycle) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded && enabled },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedCycle.persianName,
            onValueChange = { },
            readOnly = true,
            label = { Text("دوره تمدید") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(),
            enabled = enabled
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
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val theme = LocalTheme.current
    val formatter = SimpleDateFormat("dd MMM yyyy", Locale("fa", "IR"))
    
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = theme.surfaceVariant),
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
