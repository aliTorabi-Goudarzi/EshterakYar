package ir.dekot.eshterakyar

import android.app.Application
import ir.dekot.eshterakyar.core.di.appModule
import ir.dekot.eshterakyar.core.di.databaseModule
import ir.dekot.eshterakyar.core.di.nestedNavigationModule
import ir.dekot.eshterakyar.core.di.repositoryModule
import ir.dekot.eshterakyar.core.di.rootNavigationModule
import ir.dekot.eshterakyar.core.notification.NotificationHelper
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class EshterakYarApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // ایجاد کانال نوتیفیکیشن برای یادآوری‌ها
        NotificationHelper.createNotificationChannel(this)

        // Initialize Koin DI
        startKoin {
            androidLogger()
            androidContext(this@EshterakYarApplication)
            modules(
                    databaseModule,
                    repositoryModule,
                    appModule,
                    rootNavigationModule,
                    nestedNavigationModule
            )
        }
    }
}
