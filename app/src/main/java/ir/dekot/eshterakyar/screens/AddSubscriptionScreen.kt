package ir.dekot.eshterakyar.screens

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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.dekot.eshterakyar.R
import ir.dekot.eshterakyar.core.utils.LocalTheme
import ir.dekot.eshterakyar.feature_addSubscription.presentation.component.BasicInfoStep
import ir.dekot.eshterakyar.feature_addSubscription.presentation.component.PaymentStep
import ir.dekot.eshterakyar.feature_addSubscription.presentation.component.ReviewStep
import ir.dekot.eshterakyar.feature_addSubscription.presentation.component.ScheduleStep
import ir.dekot.eshterakyar.feature_addSubscription.presentation.component.StepContainer
import ir.dekot.eshterakyar.feature_addSubscription.presentation.component.StepIndicator
import ir.dekot.eshterakyar.feature_addSubscription.presentation.component.SubscriptionDetailBottomSheet
import ir.dekot.eshterakyar.feature_addSubscription.presentation.component.SubscriptionItem
import ir.dekot.eshterakyar.feature_addSubscription.presentation.intent.AddSubscriptionIntent
import ir.dekot.eshterakyar.feature_addSubscription.presentation.state.AddSubscriptionUiState
import ir.dekot.eshterakyar.feature_addSubscription.presentation.viewmodel.AddSubscriptionViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSubscriptionScreen(
    viewModel: AddSubscriptionViewModel = koinViewModel(),
    onEditSubscription: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val theme = LocalTheme.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    
    // وضعیت نمایش فرم
    // Form visibility state
    var isFormExpanded by remember { mutableStateOf(false) }
    
    // مدیریت ذخیره موفق با انیمیشن
    // Handle successful save with animation
    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            // بستن فرم
            // Collapse the form
            delay(300)
            isFormExpanded = false
            
            // نمایش پیام موفقیت
            // Show success snackbar
            delay(500)
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = "اشتراک با موفقیت اضافه شد",
                    actionLabel = null,
                    withDismissAction = true
                )
            }
            
            // ریست کردن وضعیت‌ها
            // Reset states
            delay(1000)
            viewModel.onIntent(AddSubscriptionIntent.OnSuccessDismissed)
        }
    }

    // مدیریت خطا
    // Handle error
    LaunchedEffect(uiState.error) {
        if (uiState.error != null) {
            // نمایش پیام خطا
            // Show error snackbar
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = "خطا: ${uiState.error}",
                    actionLabel = null,
                    withDismissAction = true
                )
            }
            viewModel.onIntent(AddSubscriptionIntent.OnErrorDismissed)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            // ناحیه اصلی محتوا
            // Main content area
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // نمایش دکمه و متن فقط وقتی فرم بسته است
                // Show button and text only when form is collapsed
                AnimatedVisibility(visible = !isFormExpanded) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(top = 24.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.click_to_add_subscription),
                            style = MaterialTheme.typography.bodyLarge,
                            color = theme.onSurfaceVariant,
                            modifier = Modifier.padding(16.dp)
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // دکمه افزودن
                        // Add Button
                        Button(
                            onClick = { isFormExpanded = true },
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(R.string.add_subscription_action),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                            Text(stringResource(R.string.add_subscription_action))
                        }
                    }
                }
                
                // کانتینر انیمیشن فرم
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
                    AddSubscriptionStepper(
                        uiState = uiState,
                        onIntent = viewModel::onIntent,
                        onCancel = { isFormExpanded = false },
                        modifier = Modifier.padding(16.dp)
                    )
                }

                // لیست اشتراک‌ها
                // Subscription List
                if (uiState.subscriptions.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(uiState.subscriptions, key = { it.id }) { subscription ->
                            SubscriptionItem(
                                subscription = subscription,
                                onClick = { viewModel.onIntent(AddSubscriptionIntent.OnSubscriptionClicked(subscription)) }
                            )
                        }
                        // فضای خالی پایین لیست برای دیده شدن آیتم‌های آخر
                        // Extra padding at bottom
                        item { Spacer(modifier = Modifier.height(80.dp)) }
                    }
                }
            }
        }
        
        // محل نمایش اسنک‌بار در پایین
        // Snackbar host at bottom
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp)
        ) { data ->
            CustomSnackbar(
                message = data.visuals.message,
                isError = uiState.error != null,
                onDismiss = { data.dismiss() }
            )
        }
    }

    // باتم‌شیت جزئیات اشتراک
    // Subscription Detail Bottom Sheet
    if (uiState.isBottomSheetOpen && uiState.selectedSubscription != null) {
        SubscriptionDetailBottomSheet(
            subscription = uiState.selectedSubscription!!,
            onEditClick = {
                val sub = uiState.selectedSubscription!!
                viewModel.onIntent(AddSubscriptionIntent.OnEditClicked(sub))
                onEditSubscription(sub.id)
            },
            onDeleteClick = { viewModel.onIntent(AddSubscriptionIntent.OnDeleteClicked) },
            onDismissRequest = { viewModel.onIntent(AddSubscriptionIntent.OnBottomSheetDismissed) },
            sheetState = sheetState
        )
    }

    // دیالوگ تایید حذف
    // Delete Confirmation Dialog
    if (uiState.isDeleteDialogVisible) {
        AlertDialog(
            onDismissRequest = { viewModel.onIntent(AddSubscriptionIntent.OnDeleteCancelled) },
            title = { Text(stringResource(R.string.delete_confirmation_title)) },
            text = { Text(stringResource(R.string.delete_confirmation_message)) },
            confirmButton = {
                TextButton(onClick = { viewModel.onIntent(AddSubscriptionIntent.OnDeleteConfirmed) }) {
                    Text(stringResource(R.string.delete_action), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onIntent(AddSubscriptionIntent.OnDeleteCancelled) }) {
                    Text(stringResource(R.string.cancel_action))
                }
            },
            containerColor = theme.surface,
            titleContentColor = theme.onSurface,
            textContentColor = theme.onSurfaceVariant
        )
    }
}


@Composable
private fun AddSubscriptionStepper(
    uiState: AddSubscriptionUiState,
    onIntent: (AddSubscriptionIntent) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalTheme.current
    
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = theme.surface,
            contentColor = theme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // هدر با دکمه بستن
            // Header with Close button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.add_new_subscription),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = theme.onSurface
                )
                IconButton(onClick = onCancel) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.close_action),
                        tint = theme.onSurfaceVariant
                    )
                }
            }

            // نمایشگر مرحله
            // Step Indicator
            StepIndicator(
                currentStep = uiState.currentStep,
                totalSteps = 4
            )

            // محتوای مرحله
            // Step Content
            StepContainer(
                currentStep = uiState.currentStep,
                modifier = Modifier.weight(1f, fill = false) 
            ) { step ->
                when (step) {
                    1 -> BasicInfoStep(
                        uiState = uiState,
                        onNameChange = { onIntent(AddSubscriptionIntent.OnNameChanged(it)) },
                        onCategoryChange = { onIntent(AddSubscriptionIntent.OnCategoryChanged(it)) }
                    )
                    2 -> PaymentStep(
                        uiState = uiState,
                        onPriceChange = { onIntent(AddSubscriptionIntent.OnPriceChanged(it)) },
                        onCurrencyChange = { onIntent(AddSubscriptionIntent.OnCurrencyChanged(it)) },
                        onBillingCycleChange = { onIntent(AddSubscriptionIntent.OnBillingCycleChanged(it)) }
                    )
                    3 -> ScheduleStep(
                        uiState = uiState,
                        onDateChange = { onIntent(AddSubscriptionIntent.OnNextRenewalDateChanged(it)) },
                        onActiveChange = { onIntent(AddSubscriptionIntent.OnActiveStatusChanged(it)) }
                    )
                    4 -> ReviewStep(
                        uiState = uiState,
                        onConfirm = { onIntent(AddSubscriptionIntent.OnSaveClicked) }
                    )
                }
            }

            // دکمه‌های ناوبری (فقط برای مراحل ۱ تا ۳)
            // Navigation Buttons (Only for steps 1-3)
            if (uiState.currentStep < 4) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // دکمه قبلی / انصراف
                    // Previous / Cancel Button
                    Button(
                        onClick = {
                            if (uiState.currentStep > 1) {
                                onIntent(AddSubscriptionIntent.OnPreviousStep)
                            } else {
                                onCancel()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = theme.surfaceVariant,
                            contentColor = theme.onSurfaceVariant
                        )
                    ) {
                        Text(
                            if (uiState.currentStep > 1) stringResource(R.string.previous_step) 
                            else stringResource(R.string.cancel_action)
                        )
                    }

                    // دکمه بعدی
                    // Next Button
                    Button(
                        onClick = { onIntent(AddSubscriptionIntent.OnNextStep) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.next_step))
                    }
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
            theme.error 
        else 
            theme.primaryContainer,
        contentColor = if (isError) 
            theme.onError 
        else 
            theme.onPrimaryContainer,
        action = {
            IconButton(
                onClick = { onDismiss() },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = if (isError) Icons.Default.Close else Icons.Default.Check,
                    contentDescription = stringResource(R.string.close_action),
                    tint = if (isError) 
                        theme.onError 
                    else 
                        theme.onPrimaryContainer
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
                    modifier = Modifier.size(20.dp),
                    tint = theme.onError
                )
            }
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isError) theme.onError else theme.onPrimaryContainer
            )
        }
    }
}