package ir.dekot.eshterakyar.feature_home.presentation.screen

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import ir.dekot.eshterakyar.core.navigation.BottomBarItem
import ir.dekot.eshterakyar.core.navigation.Screens
import ir.dekot.eshterakyar.core.utils.LocalTheme
import ir.dekot.eshterakyar.feature_home.presentation.components.GreetingCard
import ir.dekot.eshterakyar.feature_home.presentation.components.StatsCard
import ir.dekot.eshterakyar.feature_home.presentation.components.SubscriptionCard
import ir.dekot.eshterakyar.feature_home.presentation.viewmodel.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel = koinViewModel(),backStack : NavBackStack) {
    // توضیح: وضعیت باز/بسته بودن کارت خوش‌آمدگویی
    var isCardExpanded by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val theme = LocalTheme.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(140.dp))
        }

        // GreetingCard که روی محتوا قرار می‌گیرد
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        ) {
            GreetingCard(
                modifier = Modifier.fillMaxWidth(),
                onExpandedChange = { expanded -> isCardExpanded = expanded }
            )
        }
        Crossfade(
            targetState = uiState,
            animationSpec = tween(durationMillis = 300),
            label = "uiState"
        ) { state ->
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = theme.primary
                        )
                    }
                }

                state.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
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

                            Button(
                                onClick = { viewModel.refreshData() }
                            ) {
                                Text("تلاش مجدد")
                            }
                        }
                    }
                }

                state.subscriptions.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "هنوز اشتراکی اضافه نکرده‌اید!",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = theme.onSurface
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "برای مدیریت اشتراک‌های خود، روی دکمه + پایین صفحه ضربه بزنید",
                                style = MaterialTheme.typography.bodyMedium,
                                color = theme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = {
                                    backStack.add(BottomBarItem.AddSubscription)
                                }
                            ) {
                                Text("افزودن اولین اشتراک")
                            }
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Stats card
                        if (state.stats != null) {
                            item {
                                StatsCard(
                                    stats = state.stats,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        // Subscriptions header
                        item {
                            Text(
                                text = "اشتراک‌های فعال",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = theme.onSurface,
                                fontSize = 18.sp
                            )
                        }

                        // Subscription cards
                        items(state.subscriptions) { subscription ->
                            SubscriptionCard(
                                subscription = subscription,
                                onClick = {
                                    // Navigate to subscription details
                                    backStack.add(Screens.SubscriptionDetail(subscription.id))
                                },
                                onEdit = {
                                    // Navigate to edit subscription
                                    backStack.add(Screens.EditSubscription(subscription.id))
                                },
                                onDelete = {
                                    // Handle delete subscription
                                    viewModel.deleteSubscription(subscription)
                                },
                                onToggleStatus = {
                                    // Handle toggle subscription status
                                    viewModel.toggleSubscriptionStatus(subscription)
                                }
                            )
                        }
                    }
                }
            }
        }

    }
}
