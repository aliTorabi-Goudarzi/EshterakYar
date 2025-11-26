package ir.dekot.eshterakyar.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImage
import ir.dekot.eshterakyar.core.database.GetUserUseCase
import ir.dekot.eshterakyar.core.navigation.RootNavigator
import ir.dekot.eshterakyar.core.utils.LocalTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import sv.lib.squircleshape.CornerSmoothing
import sv.lib.squircleshape.SquircleShape
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ProfileDetailScreen(
    viewModel: ProfileDetailViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val theme = LocalTheme.current
    val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale("fa", "IR"))

    // Handle navigation errors and auto-refresh
    LaunchedEffect(uiState.error) {
        if (uiState.error != null) {
            viewModel.clearError()
            viewModel.refreshUser()
        }
    }

    Scaffold(
        topBar = {
            ProfileDetailTopBar(
                onBack = {
                    viewModel.goBack()
                },
                onEdit = {
                    // TODO: Navigate to edit profile - prevent crash for now
                }
            )
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = theme.primary
                    )
                }
            }

            uiState.user != null -> {
                ProfileDetailContent(
                    user = uiState.user!!,
                    dateFormatter = dateFormatter,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    onEditProfile = { /* TODO: Navigate to edit profile */ },
                    onDeleteAccount = { /* TODO: Show delete account dialog */ }
                )
            }

            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "اطلاعاتی برای نمایش وجود ندارد",
                            style = MaterialTheme.typography.titleMedium,
                            color = theme.onSurface
                        )

                        OutlinedButton(
                            onClick = { viewModel.refreshUser() }
                        ) {
                            Text("تلاش مجدد")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileDetailTopBar(onBack: () -> Unit, onEdit: () -> Unit) {
    val theme = LocalTheme.current

    TopAppBar(
        title = {
            Text(
                text = "اطلاعات پروفایل",
                style = MaterialTheme.typography.titleLarge,
                color = theme.onSurface
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "بازگشت",
                    tint = theme.onSurface
                )
            }
        },
        actions = {
            IconButton(onClick = onEdit) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "ویرایش پروفایل",
                    tint = theme.onSurface
                )
            }
        },
        colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
            containerColor = theme.surface,
            titleContentColor = theme.onSurface
        )
    )
}

@Composable
private fun ProfileDetailContent(
    user: ir.dekot.eshterakyar.core.database.User,
    dateFormatter: SimpleDateFormat,
    modifier: Modifier = Modifier,
    onEditProfile: () -> Unit,
    onDeleteAccount: () -> Unit
) {
    val theme = LocalTheme.current

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        // Profile Picture Section
        ProfilePictureSection(
            profilePicture = user.profilePicture,
            name = user.name,
            lastName = user.lastName
        )

        // User Information Card
        UserInformationCard(
            user = user,
            dateFormatter = dateFormatter
        )

        // Account Statistics Card
        AccountStatisticsCard()

        // Account Actions
        AccountActionsCard(
            onEditProfile = onEditProfile,
            onDeleteAccount = onDeleteAccount
        )
    }
}

@Composable
private fun ProfilePictureSection(
    profilePicture: String?,
    name: String,
    lastName: String
) {
    val theme = LocalTheme.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Profile Picture with gradient container
        Box(
            modifier = Modifier.size(140.dp)
        ) {
            // Gradient background container
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = SquircleShape(
                            smoothing = CornerSmoothing.High
                        ),
                        ambientColor = theme.primary.copy(alpha = 0.3f),
                        spotColor = theme.primary.copy(alpha = 0.3f)
                    )
                    .clip(SquircleShape())
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                theme.primary.copy(alpha = 0.1f),
                                theme.primary.copy(alpha = 0.05f),
                                theme.surfaceVariant.copy(alpha = 0.8f)
                            ),
                            radius = 120f
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Profile Image or Placeholder
                if (!profilePicture.isNullOrEmpty()) {
                    AsyncImage(
                        model = profilePicture,
                        contentDescription = "عکس پروفایل",
                        modifier = Modifier
                            .size(120.dp)
                            .shadow(
                                elevation = 4.dp,
                                shape = SquircleShape(
                                    smoothing = CornerSmoothing.Medium
                                )
                            )
                            .clip(
                                SquircleShape(
                                    smoothing = CornerSmoothing.Medium
                                )
                            ),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .shadow(
                                elevation = 4.dp,
                                shape = SquircleShape(
                                    smoothing = CornerSmoothing.Medium
                                )
                            )
                            .clip(
                                SquircleShape(
                                    smoothing = CornerSmoothing.Medium
                                )
                            )
                            .background(theme.surface),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "آواتار پیش‌فرض",
                            modifier = Modifier.size(60.dp),
                            tint = theme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // User Name with better typography
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "$name $lastName",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = theme.onSurface,
                textAlign = TextAlign.Center
            )

            // User role badge
            Box(
                modifier = Modifier
                    .clip(
                        SquircleShape(
                            topStart = 2,
                            topEnd = 2,
                            bottomStart = 2,
                            bottomEnd = 2,
                            smoothing = CornerSmoothing.Medium
                        )
                    )
                    .background(theme.primary.copy(alpha = 0.1f))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "کاربر ویژه",
                    style = MaterialTheme.typography.bodySmall,
                    color = theme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun UserInformationCard(
    user: ir.dekot.eshterakyar.core.database.User,
    dateFormatter: SimpleDateFormat
) {
    val theme = LocalTheme.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = SquircleShape(
                    topStart = 8,
                    topEnd = 8,
                    bottomStart = 12,
                    bottomEnd = 12,
                    smoothing = CornerSmoothing.Medium
                ),
                ambientColor = theme.primary.copy(alpha = 0.1f),
                spotColor = theme.primary.copy(alpha = 0.1f)
            ),
        shape = SquircleShape(
            topStart = 8,
            topEnd = 8,
            bottomStart = 12,
            bottomEnd = 12,
            smoothing = CornerSmoothing.Medium
        ),
        colors = CardDefaults.cardColors(
            containerColor = theme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .shadow(
                            elevation = 2.dp,
                            shape = SquircleShape(
                                smoothing = CornerSmoothing.High
                            )
                        )
                        .clip(
                            SquircleShape(
                                smoothing = CornerSmoothing.High
                            )
                        )
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    theme.primary,
                                    theme.primary.copy(alpha = 0.7f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "اطلاعات شخصی",
                        modifier = Modifier.size(20.dp),
                        tint = Color.White
                    )
                }

                Text(
                    text = "اطلاعات شخصی",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = theme.onSurface
                )
            }

            // Information Fields
            ModernInfoRow(
                icon = Icons.Default.Person,
                label = "نام کامل",
                value = "${user.name} ${user.lastName}",
                iconColor = theme.primary
            )

            ModernInfoRow(
                icon = Icons.Default.Phone,
                label = "شماره تماس",
                value = user.phoneNumber,
                iconColor = theme.primary
            )

            ModernInfoRow(
                icon = Icons.Default.Person,
                label = "تاریخ عضویت",
                value = dateFormatter.format(user.accountCreationDate),
                iconColor = theme.primary
            )
        }
    }
}

@Composable
private fun ModernInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    iconColor: Color = Color.Unspecified
) {
    val theme = LocalTheme.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Icon with background
        Box(
            modifier = Modifier
                .size(36.dp)
                .shadow(
                    elevation = 2.dp,
                    shape = SquircleShape(
                        smoothing = CornerSmoothing.Medium
                    )
                )
                .clip(
                    SquircleShape(
                        smoothing = CornerSmoothing.Medium
                    )
                )
                .background(
                    iconColor.copy(alpha = 0.1f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(18.dp),
                tint = iconColor
            )
        }

        // Text content
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = theme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = theme.onSurface,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    val theme = LocalTheme.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(20.dp),
            tint = theme.primary
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = theme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = theme.onSurface
            )
        }
    }
}

@Composable
private fun AccountStatisticsCard() {
    val theme = LocalTheme.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = SquircleShape(
                    topStart = 10,
                    topEnd = 10,
                    bottomStart = 15,
                    bottomEnd = 15,
                    smoothing = CornerSmoothing.Medium
                ),
                ambientColor = theme.primary.copy(alpha = 0.1f),
                spotColor = theme.primary.copy(alpha = 0.1f)
            ),
        shape = SquircleShape(
            topStart = 10,
            topEnd = 10,
            bottomStart = 15,
            bottomEnd = 15,
            smoothing = CornerSmoothing.Medium
        ),
        colors = CardDefaults.cardColors(
            containerColor = theme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .shadow(
                            elevation = 2.dp,
                            shape = SquircleShape(
                                smoothing = CornerSmoothing.High
                            )
                        )
                        .clip(
                            SquircleShape(
                                smoothing = CornerSmoothing.High
                            )
                        )
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF4CAF50), // Green
                                    Color(0xFF66BB6A)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person, // TODO: Change to appropriate statistics icon
                        contentDescription = "آمار حساب کاربری",
                        modifier = Modifier.size(20.dp),
                        tint = Color.White
                    )
                }

                Text(
                    text = "آمار حساب کاربری",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = theme.onSurface
                )
            }

            // Statistics Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ModernStatItem(
                    label = "اشتراک‌های فعال",
                    value = "0",
                    iconColor = Color(0xFF4CAF50),
                    backgroundColor = Color(0xFF4CAF50).copy(alpha = 0.1f),
                    modifier = Modifier.weight(1f)
                )

                ModernStatItem(
                    label = "مجموع هزینه ماهانه",
                    value = "۰ تومان",
                    iconColor = Color(0xFF2196F3), // Blue
                    backgroundColor = Color(0xFF2196F3).copy(alpha = 0.1f),
                    modifier = Modifier.weight(1f)
                )
            }

            // Progress Section
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "پیشرفت اشتراک‌ها",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = theme.onSurfaceVariant
                )

                // Progress Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .shadow(
                            elevation = 1.dp,
                            shape = SquircleShape(
                                smoothing = CornerSmoothing.High
                            )
                        )
                        .clip(
                            SquircleShape(
                                smoothing = CornerSmoothing.High
                            )
                        )
                        .background(theme.surfaceVariant)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.3f) // 30% progress
                            .fillMaxHeight()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFF4CAF50),
                                        Color(0xFF66BB6A)
                                    )
                                )
                            )
                    )
                }

                Text(
                    text = "۳۰٪ از سقف اشتراک‌ها استفاده شده",
                    style = MaterialTheme.typography.bodySmall,
                    color = theme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ModernStatItem(
    label: String,
    value: String,
    iconColor: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    val theme = LocalTheme.current

    Card(
        modifier = modifier,
        shape = SquircleShape(
            topStart = 12,
            topEnd = 12,
            bottomStart = 18,
            bottomEnd = 18,
            smoothing = CornerSmoothing.Medium
        ),
        colors = CardDefaults.cardColors(
            containerColor = theme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .shadow(
                        elevation = 3.dp,
                        shape = SquircleShape(
                            smoothing = CornerSmoothing.High
                        ),
                        ambientColor = iconColor.copy(alpha = 0.3f),
                        spotColor = iconColor.copy(alpha = 0.3f)
                    )
                    .clip(
                        SquircleShape(
                            smoothing = CornerSmoothing.High
                        )
                    )
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                // TODO: Add specific icons for each stat type
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = label,
                    modifier = Modifier.size(24.dp),
                    tint = iconColor
                )
            }

            // Value
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = iconColor
            )

            // Label
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = theme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    val theme = LocalTheme.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = theme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = theme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun AccountActionsCard(
    onEditProfile: () -> Unit,
    onDeleteAccount: () -> Unit
) {
    val theme = LocalTheme.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = SquircleShape(
                    topStart = 10,
                    topEnd = 10,
                    bottomStart = 15,
                    bottomEnd = 15,
                    smoothing = CornerSmoothing.Medium
                ),
                ambientColor = theme.primary.copy(alpha = 0.1f),
                spotColor = theme.primary.copy(alpha = 0.1f)
            ),
        shape = SquircleShape(
            topStart = 10,
            topEnd = 10,
            bottomStart = 15,
            bottomEnd = 15,
            smoothing = CornerSmoothing.Medium
        ),
        colors = CardDefaults.cardColors(
            containerColor = theme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .shadow(
                            elevation = 2.dp,
                            shape = SquircleShape(
                                smoothing = CornerSmoothing.High
                            )
                        )
                        .clip(
                            SquircleShape(
                                smoothing = CornerSmoothing.High
                            )
                        )
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFFFF9800), // Orange
                                    Color(0xFFFFB74D)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "عملیات حساب",
                        modifier = Modifier.size(20.dp),
                        tint = Color.White
                    )
                }

                Text(
                    text = "عملیات حساب",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = theme.onSurface
                )
            }

            // Action Buttons
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Primary Action - Edit Profile
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 3.dp,
                            shape = SquircleShape(
                                topStart = 8,
                                topEnd = 8,
                                bottomStart = 12,
                                bottomEnd = 12,
                                smoothing = CornerSmoothing.Medium
                            )
                        )
                        .clip(
                            SquircleShape(
                                topStart = 8,
                                topEnd = 8,
                                bottomStart = 12,
                                bottomEnd = 12,
                                smoothing = CornerSmoothing.Medium
                            )
                        )
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    theme.primary,
                                    theme.primary.copy(alpha = 0.8f)
                                )
                            )
                        )
                        .clickable { onEditProfile() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "ویرایش اطلاعات پروفایل",
                        modifier = Modifier.padding(vertical = 16.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }

                // Secondary Action - More Options
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Settings Option
                    SecondaryActionItem(
                        text = "تنظیمات",
                        icon = Icons.Default.Person, // TODO: Change to Settings icon
                        onClick = { /* TODO: Navigate to settings */ },
                        modifier = Modifier.weight(1f)
                    )

                    // Security Option
                    SecondaryActionItem(
                        text = "امنیت",
                        icon = Icons.Default.Person, // TODO: Change to Security icon
                        onClick = { /* TODO: Navigate to security */ },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun SecondaryActionItem(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalTheme.current

    Box(
        modifier = modifier
            .shadow(
                elevation = 2.dp,
                shape = SquircleShape(
                    smoothing = CornerSmoothing.Medium
                )
            )
            .clip(
                SquircleShape(
                    smoothing = CornerSmoothing.Medium
                )
            )
            .background(theme.surfaceVariant)
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.size(20.dp),
                tint = theme.primary
            )

            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = theme.onSurface,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// ViewModel and State classes
data class ProfileDetailUiState(
    val user: ir.dekot.eshterakyar.core.database.User? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class ProfileDetailViewModel(
    private val rootNavigator: RootNavigator,
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    fun goBack(){
        rootNavigator.goBack()
    }

    private val _uiState = MutableStateFlow(ProfileDetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val user = getUserUseCase.getCurrentUser()
                _uiState.value = _uiState.value.copy(
                    user = user,
                    isLoading = false,
                    error = if (user == null) "کاربری یافت نشد" else null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "خطا در بارگذاری اطلاعات: ${e.message}"
                )
            }
        }
    }

    fun refreshUser() {
        loadUser()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}