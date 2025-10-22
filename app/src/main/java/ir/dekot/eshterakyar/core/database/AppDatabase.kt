package ir.dekot.eshterakyar.core.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.Subscription

@Database(
    entities = [Subscription::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ir.dekot.eshterakyar.core.database.TypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun subscriptionDao(): SubscriptionDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "eshterakyar_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}