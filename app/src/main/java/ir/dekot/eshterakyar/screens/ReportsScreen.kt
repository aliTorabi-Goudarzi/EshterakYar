package ir.dekot.eshterakyar.screens

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.dekot.eshterakyar.feature_home.presentation.components.StatsCard
import ir.dekot.eshterakyar.screens.components.BudgetSettingCard
import ir.dekot.eshterakyar.screens.components.CategoryBreakdownChart
import org.koin.androidx.compose.koinViewModel

@Composable
fun ReportsScreen(viewModel: ReportsViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        Crossfade(
                targetState = uiState,
                animationSpec = tween(durationMillis = 300),
                label = "uiState"
        ) { state ->
            when {
                state.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                state.error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                    text = "خطا در بارگذاری داده‌ها",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.error
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                    text = state.error,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(onClick = { viewModel.refreshData() }) { Text("تلاش مجدد") }
                        }
                    }
                }
                else -> {
                    LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding =
                                    androidx.compose.foundation.layout.PaddingValues(
                                            start = 16.dp,
                                            top = 16.dp,
                                            end = 16.dp,
                                            bottom = 100.dp // فضای اضافی برای نوار پایین
                                    ),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Text(
                                    text = "گزارش‌ها",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                            )
                        }

                        item {
                            Text(
                                    text = "آمار و گزارش‌های مالی اشتراک‌ها",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                            )
                        }

                        // Budget setting card
                        item {
                            BudgetSettingCard(
                                    currentBudget = state.budget,
                                    currentSpent = state.stats?.totalMonthlyCost ?: 0.0,
                                    onBudgetChange = { newBudget ->
                                        viewModel.updateBudget(newBudget)
                                    },
                                    modifier = Modifier.fillMaxWidth()
                            )
                        }

                        // Stats card
                        if (state.stats != null) {
                            item {
                                StatsCard(
                                        stats = state.stats,
                                        budget = state.budget,
                                        modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        // Category breakdown chart
                        item {
                            CategoryBreakdownChart(
                                    breakdowns = state.categoryBreakdown,
                                    modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}
