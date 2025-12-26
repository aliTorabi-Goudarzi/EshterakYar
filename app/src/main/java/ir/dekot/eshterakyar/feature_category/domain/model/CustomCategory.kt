package ir.dekot.eshterakyar.feature_category.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * موجودیت دسته‌بندی سفارشی کاربر
 *
 * @property id شناسه یکتا
 * @property name نام دسته
 * @property iconName نام آیکون (از Material Icons)
 * @property colorCode کد رنگ هگز
 * @property createdAt زمان ایجاد
 */
@Entity(tableName = "custom_categories")
data class CustomCategory(
        @PrimaryKey val id: String = UUID.randomUUID().toString(),
        val name: String,
        val iconName: String = "category",
        val colorCode: String = "#607D8B",
        val createdAt: Long = System.currentTimeMillis()
)
