package ir.dekot.eshterakyar.feature_addSubscription.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "subscriptions")
data class Subscription(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String = "",
    val price: Double,
    val currency: String = "IRT", // IRT (تومان) یا USD (دلار)
    val billingCycle: BillingCycle,
    val startDate: Date,
    val nextRenewalDate: Date,
    val isActive: Boolean = true,
    val category: SubscriptionCategory,
    val icon: String? = null, // نام آیکون یا URL لوگو
    val reminderDays: Int = 3, // چند روز قبل از تمدید یادآوری بده
    val colorCode: String = "#3498db", // رنگ برای نمایش در UI
    val notes: String = ""
)

enum class BillingCycle(val persianName: String, val days: Int) {
    DAILY("روزانه", 1),
    WEEKLY("هفتگی", 7),
    MONTHLY("ماهانه", 30),
    QUARTERLY("سه ماهه", 90),
    YEARLY("سالانه", 365)
}

enum class SubscriptionCategory(val persianName: String) {
    ENTERTAINMENT("سرگرمی"),
    PRODUCTIVITY("ابزارهای کاری"),
    EDUCATION("آموزشی"),
    HEALTH("سلامت و ورزش"),
    NEWS("خبر و مجله"),
    MUSIC("موسیقی"),
    VIDEO("ویدیو و استریم"),
    CLOUD("فضای ابری"),
    OTHER("سایر")
}