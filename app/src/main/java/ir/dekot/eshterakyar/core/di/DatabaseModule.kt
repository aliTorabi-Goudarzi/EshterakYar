package ir.dekot.eshterakyar.core.di

import android.content.Context
import androidx.room.Room
import ir.dekot.eshterakyar.core.database.AppDatabase
import ir.dekot.eshterakyar.core.database.SubscriptionDao
import org.koin.dsl.module

val databaseModule = module {
    
    // Provide AppDatabase
    single<AppDatabase> {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "eshterakyar_database"
        )
        .fallbackToDestructiveMigration()
        .build()
    }
    
    // Provide SubscriptionDao
    single<SubscriptionDao> {
        get<AppDatabase>().subscriptionDao()
    }
}