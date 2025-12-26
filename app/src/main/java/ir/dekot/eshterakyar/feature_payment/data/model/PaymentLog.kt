package ir.dekot.eshterakyar.feature_payment.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.Subscription
import java.util.Date

/**
 * موجودیت لاگ پرداخت
 *
 * هر زمان که کاربر دکمه "پرداخت کردم" را فشار دهد، یک رکورد جدید ایجاد می‌شود. این جدا از تاریخ
 * شروع اشتراک است و فقط برای ردیابی تاریخچه پرداخت‌ها استفاده می‌شود.
 *
 * @param id شناسه یکتای لاگ
 * @param subscriptionId شناسه اشتراک مرتبط
 * @param paymentDate تاریخ پرداخت
 * @param amount مبلغ پرداخت شده
 * @param note یادداشت اختیاری
 */
@Entity(
        tableName = "payment_logs",
        foreignKeys =
                [
                        ForeignKey(
                                entity = Subscription::class,
                                parentColumns = ["id"],
                                childColumns = ["subscriptionId"],
                                onDelete = ForeignKey.CASCADE
                        )],
        indices = [Index("subscriptionId")]
)
data class PaymentLog(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        val subscriptionId: Long,
        val paymentDate: Date,
        val amount: Double,
        val note: String = ""
)
