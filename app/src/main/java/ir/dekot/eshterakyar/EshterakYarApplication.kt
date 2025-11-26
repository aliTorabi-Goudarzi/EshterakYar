package ir.dekot.eshterakyar

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ir.dekot.eshterakyar.core.di.appModule
import ir.dekot.eshterakyar.core.di.databaseModule
import ir.dekot.eshterakyar.core.di.nestedNavigationModule
import ir.dekot.eshterakyar.core.di.rootNavigationModule
import ir.dekot.eshterakyar.core.di.repositoryModule

class EshterakYarApplication : Application() {
    override fun onCreate() {

        super.onCreate()
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