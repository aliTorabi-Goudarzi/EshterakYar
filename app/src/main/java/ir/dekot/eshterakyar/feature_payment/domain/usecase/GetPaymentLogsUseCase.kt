package ir.dekot.eshterakyar.feature_payment.domain.usecase

import ir.dekot.eshterakyar.feature_payment.data.datasource.PaymentLogDao
import ir.dekot.eshterakyar.feature_payment.data.model.PaymentLog
import kotlinx.coroutines.flow.Flow

/** یوزکیس دریافت تاریخچه پرداخت‌های یک اشتراک */
class GetPaymentLogsUseCase(private val paymentLogDao: PaymentLogDao) {

    /**
     * دریافت لیست پرداخت‌های یک اشتراک
     *
     * @param subscriptionId شناسه اشتراک
     * @return جریان لیست پرداخت‌ها
     */
    operator fun invoke(subscriptionId: Long): Flow<List<PaymentLog>> {
        return paymentLogDao.getLogsForSubscription(subscriptionId)
    }
}
