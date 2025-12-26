package ir.dekot.eshterakyar.feature_payment.data.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ir.dekot.eshterakyar.feature_payment.data.model.PaymentLog
import kotlinx.coroutines.flow.Flow

/** DAO برای عملیات CRUD روی لاگ‌های پرداخت */
@Dao
interface PaymentLogDao {

    /** ثبت لاگ پرداخت جدید */
    @Insert suspend fun insert(paymentLog: PaymentLog): Long

    /** دریافت تمام لاگ‌های پرداخت یک اشتراک */
    @Query(
            "SELECT * FROM payment_logs WHERE subscriptionId = :subscriptionId ORDER BY paymentDate DESC"
    )
    fun getLogsForSubscription(subscriptionId: Long): Flow<List<PaymentLog>>

    /** دریافت آخرین پرداخت یک اشتراک */
    @Query(
            "SELECT * FROM payment_logs WHERE subscriptionId = :subscriptionId ORDER BY paymentDate DESC LIMIT 1"
    )
    suspend fun getLastPayment(subscriptionId: Long): PaymentLog?

    /** دریافت تعداد پرداخت‌های یک اشتراک */
    @Query("SELECT COUNT(*) FROM payment_logs WHERE subscriptionId = :subscriptionId")
    suspend fun getPaymentCount(subscriptionId: Long): Int

    /** حذف تمام لاگ‌های یک اشتراک */
    @Query("DELETE FROM payment_logs WHERE subscriptionId = :subscriptionId")
    suspend fun deleteLogsForSubscription(subscriptionId: Long)
}
