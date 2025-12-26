package ir.dekot.eshterakyar.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.Subscription
import ir.dekot.eshterakyar.feature_category.data.datasource.CategoryDao
import ir.dekot.eshterakyar.feature_category.domain.model.CustomCategory
import ir.dekot.eshterakyar.feature_payment.data.datasource.PaymentLogDao
import ir.dekot.eshterakyar.feature_payment.data.model.PaymentLog

@Database(
        entities = [Subscription::class, User::class, CustomCategory::class, PaymentLog::class],
        version = 4,
        exportSchema = false
)
@TypeConverters(ir.dekot.eshterakyar.core.database.TypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun subscriptionDao(): SubscriptionDao
    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun paymentLogDao(): PaymentLogDao
}
