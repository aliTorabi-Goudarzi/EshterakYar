package ir.dekot.eshterakyar.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import ir.dekot.eshterakyar.MainActivity
import ir.dekot.eshterakyar.R
import ir.dekot.eshterakyar.core.utils.CurrencyConverter
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.Subscription

/**
 * کلاس کمکی برای مدیریت نوتیفیکیشن‌ها
 *
 * این کلاس وظیفه ایجاد کانال نوتیفیکیشن و نمایش یادآوری‌های تمدید اشتراک را دارد
 */
object NotificationHelper {

    private const val CHANNEL_ID = "subscription_reminders"
    private const val CHANNEL_NAME = "یادآوری اشتراک‌ها"
    private const val CHANNEL_DESCRIPTION = "اطلاع‌رسانی زمان تمدید اشتراک‌ها"

    /**
     * ایجاد کانال نوتیفیکیشن (برای اندروید 8 به بالا) این متد باید در Application.onCreate فراخوانی
     * شود
     */
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel =
                    NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                        description = CHANNEL_DESCRIPTION
                        enableVibration(true)
                        enableLights(true)
                    }

            val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * نمایش نوتیفیکیشن یادآوری تمدید اشتراک
     *
     * @param context کانتکست اپلیکیشن
     * @param subscription اشتراک مورد نظر
     * @param daysRemaining تعداد روز باقی‌مانده تا تمدید
     */
    fun showRenewalReminder(context: Context, subscription: Subscription, daysRemaining: Int) {
        val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Intent برای باز کردن اپلیکیشن
        val intent =
                Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    putExtra("subscription_id", subscription.id)
                }

        val pendingIntent =
                PendingIntent.getActivity(
                        context,
                        subscription.id.toInt(),
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

        // متن نوتیفیکیشن
        val title =
                when (daysRemaining) {
                    0 -> "امروز تمدید ${subscription.name}"
                    1 -> "فردا تمدید ${subscription.name}"
                    else -> "$daysRemaining روز تا تمدید ${subscription.name}"
                }

        val formattedPrice =
                CurrencyConverter.formatPrice(subscription.price, subscription.currency)
        val content = "مبلغ: $formattedPrice - ${subscription.category.persianName}"

        val notification =
                NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_dashboard) // TODO: استفاده از آیکون نوتیفیکیشن
                        .setContentTitle(title)
                        .setContentText(content)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setCategory(NotificationCompat.CATEGORY_REMINDER)
                        .build()

        notificationManager.notify(subscription.id.toInt(), notification)
    }

    /**
     * لغو نوتیفیکیشن یک اشتراک خاص
     *
     * @param context کانتکست
     * @param subscriptionId شناسه اشتراک
     */
    fun cancelNotification(context: Context, subscriptionId: Long) {
        val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(subscriptionId.toInt())
    }
}
