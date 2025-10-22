package ir.dekot.eshterakyar.core.themePreferences

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.androidx.compose.koinViewModel

// تعریف استیت‌های مختلف برای انیمیشن
private enum class SwitchState2 {
    Checked, Unchecked
}

@Composable
fun CustomAnimatedSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    width: Dp = 65.dp,
    height: Dp = 34.dp,
    thumbColor: Color = Color.White,
    checkedTrackColor: Color = Color(0xFF35898F),
    uncheckedTrackColor: Color = Color.LightGray,
    gap: Dp = 6.dp // فاصله بین دستگیره و لبه‌ها
) {
    val internalChecked = remember { mutableStateOf(checked) }
    LaunchedEffect(checked) { internalChecked.value = checked }

    val transition = updateTransition(
        targetState = if (internalChecked.value) SwitchState2.Checked else SwitchState2.Unchecked,
        label = "Switch Transition"
    )
    val thumbSize = height - (gap * 2)

    // انیمیشن برای تغییر رنگ پس‌زمینه
    val trackColor by transition.animateColor(
        transitionSpec = {
            tween(durationMillis = 250, easing = FastOutSlowInEasing)
        },
        label = "Track Color"
    ) { state ->
        when (state) {
            SwitchState2.Checked -> checkedTrackColor
            SwitchState2.Unchecked -> uncheckedTrackColor
        }
    }

    // انیمیشن برای حرکت دستگیره در محور افقی
    val thumbOffset by transition.animateDp(
        transitionSpec = {
            spring(
                stiffness = Spring.StiffnessLow, // نرم بودن
                dampingRatio = Spring.DampingRatioMediumBouncy // کشسانی ملایم
            )
        },
        label = "Thumb Offset"
    ) { state ->
        val extraGap = if (state == SwitchState2.Checked) gap + 2.dp else gap + 2.dp
        when (state) {
            SwitchState2.Checked -> width - thumbSize - extraGap
            SwitchState2.Unchecked -> extraGap
        }
    }
    val thumbScale by transition.animateFloat(
        transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMedium
            )
        },
        label = "Thumb Scale"
    ) { state ->
        if (state == SwitchState2.Checked) 1.1f else 1.0f
    }


    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .background(color = trackColor, shape = CircleShape)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                val newValue = !internalChecked.value
                internalChecked.value = newValue
                onCheckedChange(newValue)
            },
        contentAlignment = Alignment.CenterStart
    ) {


        // دستگیره (Thumb)
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(thumbSize)
                .scale(thumbScale)
                .background(color = thumbColor, shape = CircleShape)
                .padding(4.dp) // ایجاد حاشیه داخلی
                .border(width = 30.dp, color = trackColor, shape = CircleShape)
            // .background(color = thumbColor, shape = CircleShape)
        )
    }
}


@Composable
fun SwitchPreview() {
    var isChecked by remember { mutableStateOf(false) }

    CustomAnimatedSwitch(
        checked = isChecked,
        onCheckedChange = { isChecked = it }
    )
}

@Composable
fun ThemeSwitch(modifier: Modifier = Modifier) {
    // DEBUG: Log to validate ViewModel instantiation issue
    println("DEBUG: ThemeSwitch - Attempting to create ThemeViewModel")
    val viewModel: ThemeViewModel = koinViewModel()
    println("DEBUG: ThemeSwitch - ThemeViewModel created successfully")
    val isDark by viewModel.isDarkTheme.collectAsStateWithLifecycle()

    CustomAnimatedSwitch(
        checked = isDark,
        onCheckedChange = { viewModel.toggleTheme() },
        modifier = modifier
    )
    Text(text = if (isDark) "دارک مود فعال" else "لایت مود فعال")
}