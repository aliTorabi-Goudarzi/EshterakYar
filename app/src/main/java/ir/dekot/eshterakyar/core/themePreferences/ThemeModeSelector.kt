package ir.dekot.eshterakyar.core.themePreferences

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ir.dekot.eshterakyar.core.utils.LocalTheme
import sv.lib.squircleshape.CornerSmoothing
import sv.lib.squircleshape.SquircleShape

/**
 * انتخابگر حالت تم با سه گزینه
 *
 * @param selectedMode حالت انتخاب شده فعلی
 * @param onModeSelected کالبک هنگام انتخاب حالت جدید
 * @param modifier تغییردهنده‌های ظاهری
 */
@Composable
fun ThemeModeSelector(
        selectedMode: ThemeMode,
        onModeSelected: (ThemeMode) -> Unit,
        modifier: Modifier = Modifier
) {
    val theme = LocalTheme.current

    Row(
            modifier =
                    modifier.fillMaxWidth()
                            .clip(
                                    SquircleShape(
                                            topStart = 12,
                                            topEnd = 12,
                                            bottomStart = 12,
                                            bottomEnd = 12,
                                            smoothing = CornerSmoothing.Medium
                                    )
                            )
                            .background(theme.surface),
            horizontalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        ThemeMode.entries.forEach { mode ->
            ThemeModeOption(
                    mode = mode,
                    isSelected = mode == selectedMode,
                    onClick = { onModeSelected(mode) },
                    modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * یک گزینه در انتخابگر حالت تم
 *
 * @param mode حالت تم
 * @param isSelected آیا این گزینه انتخاب شده است
 * @param onClick کالبک هنگام کلیک
 * @param modifier تغییردهنده‌های ظاهری
 */
@Composable
private fun ThemeModeOption(
        mode: ThemeMode,
        isSelected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier
) {
    val theme = LocalTheme.current

    Box(
            modifier =
                    modifier.clip(
                                    SquircleShape(
                                            topStart = 10,
                                            topEnd = 10,
                                            bottomStart = 10,
                                            bottomEnd = 10,
                                            smoothing = CornerSmoothing.Medium
                                    )
                            )
                            .background(if (isSelected) theme.primary else theme.surface)
                            .clickable { onClick() }
                            .padding(vertical = 12.dp, horizontal = 8.dp),
            contentAlignment = Alignment.Center
    ) {
        Text(
                text = mode.persianName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) theme.onPrimary else theme.onSurface
        )
    }
}
