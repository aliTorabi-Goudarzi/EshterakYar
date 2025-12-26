package ir.dekot.eshterakyar.core.utils

import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.View

/**
 * ابزار کمکی برای بازخورد لرزشی (Haptic Feedback)
 *
 * استفاده از لرزش‌های ریز برای بهبود حس تعامل با کاربر
 */
object HapticHelper {

    /** لرزش ریز برای کلیک معمولی مناسب برای دکمه‌ها و کارت‌ها */
    fun performClick(view: View) {
        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
    }

    /** لرزش متوسط برای کلیک سنگین‌تر مناسب برای عملیات‌های مهم مثل ثبت پرداخت */
    fun performHeavyClick(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_PRESS)
        } else {
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        }
    }

    /** لرزش برای تأیید موفقیت مناسب برای عملیات‌های موفق */
    fun performConfirm(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
        } else {
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        }
    }

    /** لرزش برای خطا مناسب برای نمایش خطا */
    fun performReject(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            view.performHapticFeedback(HapticFeedbackConstants.REJECT)
        } else {
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        }
    }
}
