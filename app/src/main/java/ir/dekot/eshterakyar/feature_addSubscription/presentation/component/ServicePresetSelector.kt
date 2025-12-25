package ir.dekot.eshterakyar.feature_addSubscription.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.dekot.eshterakyar.R
import ir.dekot.eshterakyar.core.utils.LocalTheme
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.BillingCycle
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.ServicePreset
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.SubscriptionCategory

/**
 * کامپوننت انتخابگر پریست‌های سرویس‌های محبوب نوار افقی اسکرول‌پذیر از کارت‌های پریست با آیکون و
 * نام
 *
 * @param presets لیست پریست‌های موجود
 * @param selectedPreset پریست انتخاب شده فعلی (ممکن است null باشد)
 * @param onPresetSelected کالبک هنگام انتخاب پریست
 * @param modifier مدیفایر کامپوز
 */
@Composable
fun ServicePresetSelector(
        presets: List<ServicePreset>,
        selectedPreset: ServicePreset?,
        onPresetSelected: (ServicePreset) -> Unit,
        modifier: Modifier = Modifier
) {
    val theme = LocalTheme.current

    Column(modifier = modifier.fillMaxWidth()) {
        // عنوان بخش
        Text(
                text = stringResource(R.string.popular_services),
                style = MaterialTheme.typography.labelMedium,
                color = theme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
        )

        // لیست افقی پریست‌ها
        LazyRow(
                contentPadding = PaddingValues(horizontal = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
        ) {
            items(presets, key = { it.id }) { preset ->
                PresetCard(
                        preset = preset,
                        isSelected = preset.id == selectedPreset?.id,
                        onClick = { onPresetSelected(preset) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // متن راهنما
        Text(
                text = stringResource(R.string.or_add_manually),
                style = MaterialTheme.typography.bodySmall,
                color = theme.onSurfaceVariant.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
        )
    }
}

/** کارت پریست منفرد شامل آیکون دایره‌ای و نام سرویس */
@Composable
private fun PresetCard(
        preset: ServicePreset,
        isSelected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier
) {
    val theme = LocalTheme.current
    val brandColor =
            try {
                Color(android.graphics.Color.parseColor(preset.colorCode))
            } catch (e: Exception) {
                theme.primary
            }

    Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
                    modifier.clip(RoundedCornerShape(12.dp))
                            .clickable(onClick = onClick)
                            .background(
                                    if (isSelected) brandColor.copy(alpha = 0.1f)
                                    else Color.Transparent,
                                    RoundedCornerShape(12.dp)
                            )
                            .padding(8.dp)
    ) {
        // آیکون دایره‌ای
        Box(
                contentAlignment = Alignment.Center,
                modifier =
                        Modifier.size(56.dp)
                                .clip(CircleShape)
                                .background(brandColor.copy(alpha = 0.15f))
                                .then(
                                        if (isSelected) {
                                            Modifier.border(2.dp, brandColor, CircleShape)
                                        } else {
                                            Modifier
                                        }
                                )
        ) {
            Icon(
                    painter = painterResource(id = preset.iconResId),
                    contentDescription = preset.name,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // نام سرویس
        Text(
                text = preset.name,
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) brandColor else theme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ServicePresetSelectorPreview() {
    val mockPresets =
            listOf(
                    ServicePreset(
                            id = "filimo",
                            name = "فیلیمو",
                            iconResId = R.drawable.ic_preset_filimo,
                            colorCode = "#FF5722",
                            defaultCategory = SubscriptionCategory.VIDEO,
                            defaultPrice = 99000.0,
                            defaultBillingCycle = BillingCycle.MONTHLY
                    ),
                    ServicePreset(
                            id = "namava",
                            name = "نماوا",
                            iconResId = R.drawable.ic_preset_namava,
                            colorCode = "#00BCD4",
                            defaultCategory = SubscriptionCategory.VIDEO
                    ),
                    ServicePreset(
                            id = "snapp",
                            name = "اسنپ",
                            iconResId = R.drawable.ic_preset_snapp,
                            colorCode = "#00C853",
                            defaultCategory = SubscriptionCategory.OTHER
                    )
            )

    ServicePresetSelector(
            presets = mockPresets,
            selectedPreset = mockPresets[0],
            onPresetSelected = {}
    )
}
