package ir.dekot.eshterakyar.feature_addSubscription.presentation.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * کانتینر برای مدیریت انیمیشن‌های تغییر مرحله
 * Container to handle step transition animations
 */
@Composable
fun StepContainer(
    currentStep: Int,
    modifier: Modifier = Modifier,
    content: @Composable (Int) -> Unit
) {
    AnimatedContent(
        targetState = currentStep,
        transitionSpec = {
            stepTransitionSpec(targetState > initialState)
        },
        modifier = modifier,
        label = "StepAnimation"
    ) { targetStep ->
        content(targetStep)
    }
}

/**
 * تنظیمات انیمیشن تغییر مرحله
 * Step transition animation specification
 */
private fun stepTransitionSpec(isMovingForward: Boolean): ContentTransform {
    val animationDuration = 400
    return if (isMovingForward) {
        (slideInHorizontally(animationSpec = tween(animationDuration)) { width -> width } + 
         fadeIn(animationSpec = tween(animationDuration))).togetherWith(
            slideOutHorizontally(animationSpec = tween(animationDuration)) { width -> -width } + 
            fadeOut(animationSpec = tween(animationDuration))
        )
    } else {
        (slideInHorizontally(animationSpec = tween(animationDuration)) { width -> -width } + 
         fadeIn(animationSpec = tween(animationDuration))).togetherWith(
            slideOutHorizontally(animationSpec = tween(animationDuration)) { width -> width } + 
            fadeOut(animationSpec = tween(animationDuration))
        )
    }.using(
        SizeTransform(clip = false)
    )
}
