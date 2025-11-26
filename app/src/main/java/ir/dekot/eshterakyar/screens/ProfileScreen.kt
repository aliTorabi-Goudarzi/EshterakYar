package ir.dekot.eshterakyar.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import ir.dekot.eshterakyar.core.database.User
import ir.dekot.eshterakyar.core.navigation.RootNavigator
import ir.dekot.eshterakyar.core.navigation.Screens
import ir.dekot.eshterakyar.core.utils.LocalTheme
import org.koin.androidx.compose.koinViewModel
import sv.lib.squircleshape.CornerSmoothing
import sv.lib.squircleshape.SquircleShape

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val theme = LocalTheme.current

    // Handle navigation errors
    LaunchedEffect(uiState.error) {
        if (uiState.error != null) {
            viewModel.clearError()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Banner Section - Exactly 1/3 of screen height with bottom squircle shape
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // This makes it exactly 1/3 of the available space
                .clip(
                    SquircleShape(
                        topStart = 0,
                        topEnd = 0,
                        bottomStart = 35,
                        bottomEnd = 35,
                        smoothing = CornerSmoothing.High
                    )
                )
                .clickable {
                }
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            theme.primary.copy(alpha = 0.9f),
                            theme.primary.copy(alpha = 0.7f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            ProfileBannerContent(
                user = uiState.user,
                isLoading = uiState.isLoading,
            )
        }

        // Bottom Section - Exactly 2/3 of screen height with floating menu items
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f) // This makes it exactly 2/3 of the available space
        ) {
            ProfileBottomSheetContent(
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun ProfileBannerContent(
    user: User?,
    isLoading: Boolean
) {
    val theme = LocalTheme.current

    if (isLoading) {
        CircularProgressIndicator(
            color = Color.White,
            modifier = Modifier.size(48.dp)
        )
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Picture Squircle Cut-out
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(SquircleShape())
                    .background(Color.White)
                    .border(4.dp, Color.White, SquircleShape()),
                contentAlignment = Alignment.Center
            ) {
                if (user?.profilePicture != null) {
                    AsyncImage(
                        model = user.profilePicture,
                        contentDescription = "عکس پروفایل",
                        modifier = Modifier.size(80.dp),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "عکس پروفایل",
                        modifier = Modifier.size(80.dp),
                        tint = theme.onSurfaceVariant
                    )
                }
            }

            // User Name
            Text(
                text = if (user != null) "${user.name} ${user.lastName}" else "کاربر مهمان",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ProfileBottomSheetContent(
    viewModel: ProfileViewModel
) {
    val theme = LocalTheme.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Personal Information - Floating Menu Item
        FloatingProfileMenuItem(
            icon = Icons.Default.Person,
            title = "اطلاعات شخصی",
            subtitle = "مشاهده و ویرایش اطلاعات پروفایل",
            onClick = {
            }
        )

        // Settings - Floating Menu Item
        FloatingProfileMenuItem(
            icon = Icons.Default.Settings,
            title = "تنظیمات",
            subtitle = "تنظیمات برنامه و نمایش",
            onClick = {
                viewModel.navigateToSettings()
            }
        )

        // Support - Floating Menu Item
        FloatingProfileMenuItem(
            icon = Icons.AutoMirrored.Filled.Help,
            title = "پشتیبانی",
            subtitle = "تماس با تیم پشتیبانی و راهنما",
            onClick = { /* TODO: Navigate to support */ }
        )

        // About Us - Floating Menu Item
        FloatingProfileMenuItem(
            icon = Icons.Default.Info,
            title = "درباره ما",
            subtitle = "اطلاعات بیشتر درباره اپلیکیشن",
            onClick = { /* TODO: Navigate to about */ }
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun FloatingProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    val theme = LocalTheme.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = SquircleShape()
            )
            .clip(SquircleShape())
            .background(theme.surfaceVariant)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(24.dp),
                tint = theme.primary
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = theme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = theme.onSurfaceVariant
                )
            }
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = "باز کردن",
            modifier = Modifier.size(20.dp),
            tint = theme.onSurfaceVariant
        )
    }
}