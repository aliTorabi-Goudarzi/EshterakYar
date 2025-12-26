package ir.dekot.eshterakyar.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.dekot.eshterakyar.core.navigation.RootNavigator
import ir.dekot.eshterakyar.core.themePreferences.ThemeModeSelector
import ir.dekot.eshterakyar.core.themePreferences.ThemeViewModel
import ir.dekot.eshterakyar.core.utils.Currency
import ir.dekot.eshterakyar.core.utils.LocalTheme
import org.koin.androidx.compose.koinViewModel
import sv.lib.squircleshape.CornerSmoothing
import sv.lib.squircleshape.SquircleShape

@Composable
fun SettingsScreen(
        rootNavigator: RootNavigator,
        themeViewModel: ThemeViewModel = koinViewModel(),
        settingsViewModel: SettingsViewModel = koinViewModel()
) {
    val themeMode by themeViewModel.themeMode.collectAsStateWithLifecycle()
    val settingsState by settingsViewModel.uiState.collectAsStateWithLifecycle()
    val theme = LocalTheme.current

    Scaffold(topBar = { SettingsTopBar(onBack = { rootNavigator.goBack() }) }) { paddingValues ->
        Column(
                modifier =
                        Modifier.fillMaxSize()
                                .padding(paddingValues)
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Theme Setting Card
            SettingsCard(
                    title = "ظاهر برنامه",
                    icon =
                            when (themeMode) {
                                ir.dekot.eshterakyar.core.themePreferences.ThemeMode.DARK ->
                                        Icons.Default.DarkMode
                                ir.dekot.eshterakyar.core.themePreferences.ThemeMode.LIGHT ->
                                        Icons.Default.LightMode
                                ir.dekot.eshterakyar.core.themePreferences.ThemeMode.SYSTEM ->
                                        Icons.Default.Settings
                            }
            ) {
                Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                            text = "حالت نمایش",
                            style = MaterialTheme.typography.bodyMedium,
                            color = theme.onSurfaceVariant
                    )
                    ThemeModeSelector(
                            selectedMode = themeMode,
                            onModeSelected = { mode -> themeViewModel.setThemeMode(mode) }
                    )
                }
            }

            // Currency Setting Card
            SettingsCard(title = "واحد پول پیش‌فرض", icon = Icons.Default.AttachMoney) {
                CurrencySelector(
                        selectedCurrency = settingsState.preferredCurrency,
                        onCurrencySelected = { currency ->
                            settingsViewModel.setPreferredCurrency(currency)
                        }
                )
            }

            // Notifications Setting Card (Placeholder)
            SettingsCard(title = "اطلاعیه‌ها", icon = Icons.Default.Notifications) {
                Text(
                        text = "مدیریت نوتیفیکیشن‌ها و یادآوری‌ها",
                        style = MaterialTheme.typography.bodyMedium,
                        color = theme.onSurfaceVariant
                )
            }

            // General Settings Card (Placeholder)
            SettingsCard(title = "تنظیمات کلی", icon = Icons.Default.Settings) {
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

/** انتخابگر ارز پیش‌فرض */
@Composable
private fun CurrencySelector(selectedCurrency: Currency, onCurrencySelected: (Currency) -> Unit) {
    val theme = LocalTheme.current
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(
                text = "ارز مورد نظر خود را انتخاب کنید",
                style = MaterialTheme.typography.bodySmall,
                color = theme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
        )

        Box {
            // نمایش ارز انتخاب شده
            Row(
                    modifier =
                            Modifier.fillMaxWidth()
                                    .clip(
                                            SquircleShape(
                                                    topStart = 8,
                                                    topEnd = 8,
                                                    bottomStart = 8,
                                                    bottomEnd = 8,
                                                    smoothing = CornerSmoothing.Medium
                                            )
                                    )
                                    .background(theme.surface)
                                    .clickable { expanded = true }
                                    .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                            text = selectedCurrency.symbol,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = theme.primary
                    )
                    Column {
                        Text(
                                text = selectedCurrency.persianName,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                                color = theme.onSurface
                        )
                        Text(
                                text = selectedCurrency.code,
                                style = MaterialTheme.typography.bodySmall,
                                color = theme.onSurfaceVariant
                        )
                    }
                }
                Text(
                        text = "تغییر",
                        style = MaterialTheme.typography.labelMedium,
                        color = theme.primary,
                        fontWeight = FontWeight.Medium
                )
            }

            // منوی کشویی انتخاب ارز
            DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(theme.surface)
            ) {
                Currency.entries.forEach { currency ->
                    DropdownMenuItem(
                            text = {
                                Row(
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                            text = currency.symbol,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color =
                                                    if (currency == selectedCurrency) theme.primary
                                                    else theme.onSurface
                                    )
                                    Column {
                                        Text(
                                                text = currency.persianName,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = theme.onSurface
                                        )
                                        Text(
                                                text = currency.code,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = theme.onSurfaceVariant
                                        )
                                    }
                                }
                            },
                            onClick = {
                                onCurrencySelected(currency)
                                expanded = false
                            },
                            trailingIcon = {
                                if (currency == selectedCurrency) {
                                    Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "انتخاب شده",
                                            tint = theme.primary
                                    )
                                }
                            }
                    )
                }
            }
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
            colors =
                    androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
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
            colors = CardDefaults.cardColors(containerColor = theme.surfaceVariant),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
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
