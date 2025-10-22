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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import ir.dekot.eshterakyar.core.navigation.BottomBarItem
import ir.dekot.eshterakyar.core.navigation.Screens
import ir.dekot.eshterakyar.core.utils.LocalTheme
import ir.dekot.eshterakyar.feature_home.presentation.components.CompactStatsCard
import ir.dekot.eshterakyar.feature_home.presentation.components.GreetingCard
import ir.dekot.eshterakyar.feature_home.presentation.components.SubscriptionCard
import ir.dekot.eshterakyar.feature_home.presentation.viewmodel.HomeViewModel
import org.koin.androidx.compose.koinViewModel
import kotlin.math.max
import kotlin.math.min

@Composable
fun HomeScreen(viewModel: HomeViewModel = koinViewModel(), backStack: NavBackStack,
               navigateToEditSubscription : (Long) -> Unit, navigateToSubscriptionDetail : (Long) -> Unit) {
    // توضیح: وضعیت باز/بسته بودن کارت خوش‌آمدگویی
    var isCardExpanded by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val theme = LocalTheme.current
    
    // Scroll state for tracking scroll position
    val listState = rememberLazyListState()
    
    // Height of the collapsed GreetingCard and bottom bar
    val collapsedCardHeight = 140.dp
    val bottomBarHeight = 80.dp
    val density = LocalDensity.current
    val collapsedCardHeightPx = with(density) { collapsedCardHeight.toPx() }
    val bottomBarHeightPx = with(density) { bottomBarHeight.toPx() }
    
    // Calculate the parallax offset based on scroll position
    val topParallaxOffset = remember {
        mutableStateOf(0f)
    }
    
    // Calculate bottom parallax offset for content to scroll under bottom bar
    val bottomParallaxOffset = remember {
        mutableStateOf(0f)
    }
    
    // Update parallax offsets when scrolling
    LaunchedEffect(listState.firstVisibleItemScrollOffset) {
        val scrollOffset = listState.firstVisibleItemScrollOffset.toFloat()
        
        // Create parallax effect for top: content moves up slower than scroll
        topParallaxOffset.value = min(scrollOffset * 0.5f, collapsedCardHeightPx)
        
        // Create parallax effect for bottom: content can scroll under bottom bar after initial scroll
        // Start allowing content to go under bottom bar after scrolling past the top card
        bottomParallaxOffset.value = if (scrollOffset > collapsedCardHeightPx) {
            min((scrollOffset - collapsedCardHeightPx) * 0.3f, bottomBarHeightPx)
        } else {
            0f
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // GreetingCard that stays fixed at the top
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .zIndex(9f)
        ) {
            GreetingCard(
                modifier = Modifier.fillMaxWidth(),
                onExpandedChange = { expanded -> isCardExpanded = expanded }
            )
        }
        
        // Main content with parallax effect
        Crossfade(
            targetState = uiState,
            animationSpec = tween(durationMillis = 300),
            label = "uiState"
        ) { state ->
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .offset(
                                y = with(density) { -topParallaxOffset.value.toDp() },
                                x = 0.dp
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = theme.primary
                        )
                    }
                }

                state.error != null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .offset(
                                y = with(density) { -topParallaxOffset.value.toDp() },
                                x = 0.dp
                            ),
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
                        modifier = Modifier
                            .fillMaxSize()
                            .offset(
                                y = with(density) { -topParallaxOffset.value.toDp() },
                                x = 0.dp
                            ),
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
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .offset(
                                y = with(density) { -topParallaxOffset.value.toDp() },
                                x = 0.dp
                            ),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = collapsedCardHeight + 16.dp,
                            bottom = bottomBarHeight + 16.dp - with(density) { bottomParallaxOffset.value.toDp() }
                        ),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Compact stats card
                        if (state.stats != null) {
                            item {
                                CompactStatsCard(
                                    totalBudget = 500000.0, // 500,000 تومان
                                    currentSpent = state.stats.totalMonthlyCost,
                                    activeCount = state.stats.activeCount,
                                    inactiveCount = state.inactiveCount,
                                    nearingRenewalCount = state.nearingRenewalCount,
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
                                    navigateToSubscriptionDetail(subscription.id)
                                },
                                onEdit = {
                                    navigateToEditSubscription(subscription.id)
                                    // Navigate to edit subscription
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
