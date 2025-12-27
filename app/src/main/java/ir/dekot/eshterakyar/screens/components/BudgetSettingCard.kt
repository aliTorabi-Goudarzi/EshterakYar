package ir.dekot.eshterakyar.screens.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ir.dekot.eshterakyar.core.utils.LocalTheme
import sv.lib.squircleshape.CornerSmoothing
import sv.lib.squircleshape.SquircleShape

/**
 * کارت تنظیم بودجه ماهانه
 *
 * کاربر می‌تواند بودجه ماهانه خود را تنظیم کند و وضعیت خرج‌کرد فعلی را مشاهده نماید
 *
 * @param currentBudget بودجه فعلی
 * @param currentSpent مبلغ خرج شده
 * @param onBudgetChange کالبک تغییر بودجه
 * @param modifier مادیفایر
 */
@Composable
fun BudgetSettingCard(
        currentBudget: Double,
        currentSpent: Double,
        onBudgetChange: (Double) -> Unit,
        modifier: Modifier = Modifier
) {
    val theme = LocalTheme.current
    val focusManager = LocalFocusManager.current

    // وضعیت حالت ویرایش
    var isEditing by remember { mutableStateOf(false) }
    var budgetInput by remember(currentBudget) { mutableStateOf(currentBudget.toLong().toString()) }

    // محاسبه درصد استفاده از بودجه
    val usagePercentage =
            if (currentBudget > 0) {
                (currentSpent / currentBudget).coerceAtMost(1.0)
            } else 0.0

    // انیمیشن نوار پیشرفت
    val animatedProgress by
            animateFloatAsState(
                    targetValue = usagePercentage.toFloat(),
                    animationSpec = tween(durationMillis = 800),
                    label = "budget_progress"
            )

    // رنگ نوار پیشرفت بر اساس درصد استفاده
    val progressColor =
            when {
                usagePercentage >= 1.0 -> Color(0xFFF44336) // قرمز - بیش از بودجه
                usagePercentage >= 0.8 -> Color(0xFFFF9800) // نارنجی - نزدیک به حد
                usagePercentage >= 0.5 -> Color(0xFFFFC107) // زرد - نیمه راه
                else -> Color(0xFF4CAF50) // سبز - خوب
            }

    Card(
            modifier = modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = theme.primaryContainer),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = SquircleShape(radius = 20.dp, smoothing = CornerSmoothing.Medium)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            // هدر کارت
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                            imageVector = Icons.Default.Wallet,
                            contentDescription = "بودجه",
                            tint = theme.onPrimaryContainer,
                            modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                            text = "تنظیم بودجه ماهانه",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = theme.onPrimaryContainer
                    )
                }

                // دکمه ویرایش
                IconButton(
                        onClick = {
                            if (isEditing) {
                                // ذخیره بودجه جدید
                                val newBudget = budgetInput.toDoubleOrNull()
                                if (newBudget != null && newBudget > 0) {
                                    onBudgetChange(newBudget)
                                    isEditing = false
                                    focusManager.clearFocus()
                                }
                            } else {
                                isEditing = true
                            }
                        }
                ) {
                    Icon(
                            imageVector =
                                    if (isEditing) Icons.Default.Check else Icons.Default.Edit,
                            contentDescription = if (isEditing) "ذخیره" else "ویرایش",
                            tint = if (isEditing) Color(0xFF4CAF50) else theme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // بخش نمایش یا ویرایش بودجه
            AnimatedVisibility(visible = !isEditing, enter = fadeIn(), exit = fadeOut()) {
                Column {
                    // نمایش بودجه فعلی
                    Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                                text = "بودجه ماهانه:",
                                style = MaterialTheme.typography.bodyMedium,
                                color = theme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                        Text(
                                text = formatBudget(currentBudget),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = theme.onPrimaryContainer
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // نمایش خرج شده
                    Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                                text = "خرج شده:",
                                style = MaterialTheme.typography.bodyMedium,
                                color = theme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                        Text(
                                text = formatBudget(currentSpent),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                                color = progressColor
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // نمایش باقی‌مانده
                    val remaining = currentBudget - currentSpent
                    Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                                text = "باقی‌مانده:",
                                style = MaterialTheme.typography.bodyMedium,
                                color = theme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                        Text(
                                text =
                                        if (remaining >= 0) formatBudget(remaining)
                                        else "-${formatBudget(-remaining)}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                                color = if (remaining >= 0) Color(0xFF4CAF50) else Color(0xFFF44336)
                        )
                    }
                }
            }

            // بخش ویرایش بودجه
            AnimatedVisibility(visible = isEditing, enter = fadeIn(), exit = fadeOut()) {
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                            value = budgetInput,
                            onValueChange = { newValue ->
                                // فقط اعداد را قبول کن
                                if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                                    budgetInput = newValue
                                }
                            },
                            label = { Text("بودجه ماهانه (تومان)") },
                            keyboardOptions =
                                    KeyboardOptions(
                                            keyboardType = KeyboardType.Number,
                                            imeAction = ImeAction.Done
                                    ),
                            keyboardActions =
                                    KeyboardActions(
                                            onDone = {
                                                val newBudget = budgetInput.toDoubleOrNull()
                                                if (newBudget != null && newBudget > 0) {
                                                    onBudgetChange(newBudget)
                                                    isEditing = false
                                                    focusManager.clearFocus()
                                                }
                                            }
                                    ),
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                    )

                    // دکمه لغو
                    IconButton(
                            onClick = {
                                budgetInput = currentBudget.toLong().toString()
                                isEditing = false
                                focusManager.clearFocus()
                            }
                    ) {
                        Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "لغو",
                                tint = Color(0xFFF44336)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // نوار پیشرفت بودجه
            Column {
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                            text = "مصرف بودجه",
                            style = MaterialTheme.typography.bodySmall,
                            color = theme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                    Text(
                            text = "${(usagePercentage * 100).toInt()}%",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = progressColor
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                        modifier =
                                Modifier.fillMaxWidth()
                                        .height(12.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(theme.onPrimaryContainer.copy(alpha = 0.2f))
                ) {
                    LinearProgressIndicator(
                            progress = { animatedProgress },
                            modifier =
                                    Modifier.fillMaxWidth()
                                            .height(12.dp)
                                            .clip(RoundedCornerShape(6.dp)),
                            color = progressColor,
                            trackColor = Color.Transparent
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // پیام وضعیت
                Text(
                        text =
                                when {
                                    usagePercentage >= 1.0 -> "⚠️ بودجه شما تمام شده است!"
                                    usagePercentage >= 0.8 -> "⚡ به حد بودجه نزدیک می‌شوید"
                                    usagePercentage >= 0.5 -> "📊 نیمی از بودجه استفاده شده"
                                    else -> "✅ وضعیت بودجه مناسب است"
                                },
                        style = MaterialTheme.typography.bodySmall,
                        color = progressColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

/** فرمت‌دهی مبلغ بودجه به صورت خوانا */
private fun formatBudget(amount: Double): String {
    val formatted = String.format("%,.0f", amount)
    return "$formatted تومان"
}
