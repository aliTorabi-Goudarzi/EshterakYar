package ir.dekot.eshterakyar.core.di

import androidx.room.Room
import ir.dekot.eshterakyar.core.database.AppDatabase
import ir.dekot.eshterakyar.core.database.SubscriptionDao
import ir.dekot.eshterakyar.core.database.UserDao
import ir.dekot.eshterakyar.feature_category.data.datasource.CategoryDao
import ir.dekot.eshterakyar.feature_payment.data.datasource.PaymentLogDao
import org.koin.dsl.module

val databaseModule = module {

    // Provide AppDatabase
    single<AppDatabase> {
        Room.databaseBuilder(
                        context = get(),
                        klass = AppDatabase::class.java,
                        name = "eshterakyar_database"
                )
                .fallbackToDestructiveMigration()
                .build()
    }

    // Provide SubscriptionDao
    single<SubscriptionDao> { get<AppDatabase>().subscriptionDao() }

    // Provide UserDao
    single<UserDao> { get<AppDatabase>().userDao() }

    // Provide CategoryDao
    single<CategoryDao> { get<AppDatabase>().categoryDao() }

    // Provide PaymentLogDao
    single<PaymentLogDao> { get<AppDatabase>().paymentLogDao() }
}
