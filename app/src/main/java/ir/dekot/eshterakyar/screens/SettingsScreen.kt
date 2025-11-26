package ir.dekot.eshterakyar.screens

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import ir.dekot.eshterakyar.core.navigation.RootNavigator
import ir.dekot.eshterakyar.core.themePreferences.ThemeSwitch
import ir.dekot.eshterakyar.core.themePreferences.ThemeViewModel
import ir.dekot.eshterakyar.core.utils.LocalTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    rootNavigator: RootNavigator,
    viewModel: ThemeViewModel = koinViewModel()
) {
    val isDark by viewModel.isDarkTheme.collectAsStateWithLifecycle()
    val theme = LocalTheme.current

    Scaffold(
        topBar = {
            SettingsTopBar(onBack = { rootNavigator.goBack() })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Theme Setting Card
            SettingsCard(
                title = "ظاهر برنامه",
                icon = if (isDark) Icons.Default.DarkMode else Icons.Default.LightMode
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "حالت تاریک",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = theme.onSurface
                        )
                        Text(
                            text = if (isDark) "فعال" else "غیرفعال",
                            style = MaterialTheme.typography.bodyMedium,
                            color = theme.onSurfaceVariant
                        )
                    }
                    ThemeSwitch(modifier = Modifier.scale(0.8f))
                }
            }

            // Notifications Setting Card (Placeholder)
            SettingsCard(
                title = "اطلاعیه‌ها",
                icon = Icons.Default.Notifications
            ) {
                Text(
                    text = "مدیریت نوتیفیکیشن‌ها و یادآوری‌ها",
                    style = MaterialTheme.typography.bodyMedium,
                    color = theme.onSurfaceVariant
                )
            }

            // General Settings Card (Placeholder)
            SettingsCard(
                title = "تنظیمات کلی",
                icon = Icons.Default.Settings
            ) {
                Text(
                    text = "سایر تنظیمات برنامه",
                    style = MaterialTheme.typography.bodyMedium,
                    color = theme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsTopBar(onBack: () -> Unit) {
    val theme = LocalTheme.current

    TopAppBar(
        title = {
            Text(
                text = "تنظیمات",
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
        colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
            containerColor = theme.surface,
            titleContentColor = theme.onSurface
        )
    )
}

@Composable
private fun SettingsCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable () -> Unit
) {
    val theme = LocalTheme.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = theme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    modifier = Modifier.size(24.dp),
                    tint = theme.primary
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = theme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            content()
        }
    }
}