package ir.dekot.eshterakyar.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.Subscription

@Database(
    entities = [Subscription::class, User::class],
    version = 2,
    exportSchema = true
)

@TypeConverters(ir.dekot.eshterakyar.core.database.TypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun subscriptionDao(): SubscriptionDao
    abstract fun userDao(): UserDao
}