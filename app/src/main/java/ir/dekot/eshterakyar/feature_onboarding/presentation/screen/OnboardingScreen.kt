package ir.dekot.eshterakyar.feature_onboarding.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ir.dekot.eshterakyar.core.utils.LocalTheme
import kotlinx.coroutines.launch

/** صفحه آنبوردینگ برای کاربران جدید شامل ۳ اسلاید آموزشی */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(onComplete: () -> Unit) {
    val theme = LocalTheme.current
    val scope = rememberCoroutineScope()

    val pages =
            listOf(
                    OnboardingPage(
                            title = "مدیریت اشتراک‌ها",
                            description = "همه اشتراک‌های خود را در یک جا مدیریت کنید",
                            icon = Icons.Default.Star,
                            color = Color(0xFF4CAF50)
                    ),
                    OnboardingPage(
                            title = "یادآوری تمدید",
                            description = "هرگز تاریخ تمدید اشتراک‌ها را فراموش نکنید",
                            icon = Icons.Default.Notifications,
                            color = Color(0xFF2196F3)
                    ),
                    OnboardingPage(
                            title = "گزارش هزینه‌ها",
                            description = "هزینه‌های ماهانه و سالانه خود را ببینید",
                            icon = Icons.Default.DateRange,
                            color = Color(0xFFFF9800)
                    )
            )

    val pagerState = rememberPagerState(pageCount = { pages.size })

    Box(modifier = Modifier.fillMaxSize().background(theme.backgroundColor)) {
        Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Skip button
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onComplete) {
                    Text(text = "رد شدن", color = theme.onSurfaceVariant)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Pager
            HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
                OnboardingPageContent(page = pages[page], modifier = Modifier.fillMaxSize())
            }

            // Indicators
            Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(pages.size) { index ->
                    Box(
                            modifier =
                                    Modifier.size(
                                                    if (pagerState.currentPage == index) 12.dp
                                                    else 8.dp
                                            )
                                            .clip(CircleShape)
                                            .background(
                                                    if (pagerState.currentPage == index)
                                                            theme.primary
                                                    else theme.onSurfaceVariant.copy(alpha = 0.3f)
                                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Buttons
            AnimatedVisibility(visible = pagerState.currentPage == pages.size - 1) {
                Button(
                        onClick = onComplete,
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = theme.primary)
                ) {
                    Text(
                            text = "شروع کنید!",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                    )
                }
            }

            AnimatedVisibility(visible = pagerState.currentPage < pages.size - 1) {
                Button(
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = theme.primary)
                ) {
                    Text(
                            text = "بعدی",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun OnboardingPageContent(page: OnboardingPage, modifier: Modifier = Modifier) {
    val theme = LocalTheme.current

    Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
    ) {
        // Icon
        Box(
                modifier =
                        Modifier.size(120.dp)
                                .clip(CircleShape)
                                .background(page.color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
        ) {
            Icon(
                    imageVector = page.icon,
                    contentDescription = page.title,
                    modifier = Modifier.size(64.dp),
                    tint = page.color
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Title
        Text(
                text = page.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = theme.onSurface,
                textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Description
        Text(
                text = page.description,
                style = MaterialTheme.typography.bodyLarge,
                color = theme.onSurfaceVariant,
                textAlign = TextAlign.Center
        )
    }
}

private data class OnboardingPage(
        val title: String,
        val description: String,
        val icon: ImageVector,
        val color: Color
)
