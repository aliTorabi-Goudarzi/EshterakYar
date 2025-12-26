package ir.dekot.eshterakyar.feature_payment.domain.usecase

import ir.dekot.eshterakyar.feature_payment.data.datasource.PaymentLogDao
import ir.dekot.eshterakyar.feature_payment.data.model.PaymentLog
import java.util.Date

/**
 * یوزکیس ثبت پرداخت دستی
 *
 * این یوزکیس برای ثبت اینکه کاربر یک دوره اشتراک را پرداخت کرده استفاده می‌شود. این ثبت مستقل از
 * تاریخ شروع اشتراک است و فقط برای ردیابی تاریخچه پرداخت‌ها است.
 */
class RecordPaymentUseCase(private val paymentLogDao: PaymentLogDao) {

    /**
     * ثبت پرداخت جدید
     *
     * @param subscriptionId شناسه اشتراک
     * @param amount مبلغ پرداخت شده
     * @param note یادداشت اختیاری
     * @return شناسه لاگ پرداخت ایجاد شده
     */
    suspend operator fun invoke(subscriptionId: Long, amount: Double, note: String = ""): Long {
        val paymentLog =
                PaymentLog(
                        subscriptionId = subscriptionId,
                        paymentDate = Date(),
                        amount = amount,
                        note = note
                )
        return paymentLogDao.insert(paymentLog)
    }
}
