package ir.dekot.eshterakyar

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ir.dekot.eshterakyar.core.di.appModule
import ir.dekot.eshterakyar.core.di.databaseModule
import ir.dekot.eshterakyar.core.di.repositoryModule

class EshterakYarApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // DEBUG: Log to validate Koin initialization
        println("DEBUG: EshterakYarApplication - Starting Koin initialization")
        try {
            // Initialize Koin DI
            startKoin {
                androidLogger()
                androidContext(this@EshterakYarApplication)
                modules(
                    databaseModule,
                    repositoryModule,
                    appModule
                )
            }
            println("DEBUG: EshterakYarApplication - Koin initialized successfully")
        } catch (e: Exception) {
            println("ERROR: EshterakYarApplication - Koin initialization failed: ${e.message}")
            e.printStackTrace()
        }
    }
}