package ir.dekot.eshterakyar.core.database

import androidx.room.TypeConverter
import java.util.Date
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.BillingCycle
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.SubscriptionCategory

class TypeConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
    
    @TypeConverter
    fun fromBillingCycle(billingCycle: BillingCycle): String {
        return billingCycle.name
    }
    
    @TypeConverter
    fun toBillingCycle(billingCycleString: String): BillingCycle {
        return BillingCycle.valueOf(billingCycleString)
    }
    
    @TypeConverter
    fun fromSubscriptionCategory(category: SubscriptionCategory): String {
        return category.name
    }
    
    @TypeConverter
    fun toSubscriptionCategory(categoryString: String): SubscriptionCategory {
        return SubscriptionCategory.valueOf(categoryString)
    }
}