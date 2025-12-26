package ir.dekot.eshterakyar.feature_category.data.datasource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ir.dekot.eshterakyar.feature_category.domain.model.CustomCategory
import kotlinx.coroutines.flow.Flow

/** DAO برای عملیات CRUD روی دسته‌بندی‌های سفارشی */
@Dao
interface CategoryDao {
    /** دریافت همه دسته‌بندی‌های سفارشی */
    @Query("SELECT * FROM custom_categories ORDER BY createdAt ASC")
    fun getAllCustomCategories(): Flow<List<CustomCategory>>

    /** دریافت همه دسته‌بندی‌های سفارشی به صورت لیست */
    @Query("SELECT * FROM custom_categories ORDER BY createdAt ASC")
    suspend fun getAllCustomCategoriesList(): List<CustomCategory>

    /** دریافت یک دسته با شناسه */
    @Query("SELECT * FROM custom_categories WHERE id = :id")
    suspend fun getCategoryById(id: String): CustomCategory?

    /** افزودن دسته جدید */
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(category: CustomCategory)

    /** به‌روزرسانی دسته */
    @Update suspend fun update(category: CustomCategory)

    /** حذف دسته */
    @Delete suspend fun delete(category: CustomCategory)

    /** حذف دسته با شناسه */
    @Query("DELETE FROM custom_categories WHERE id = :id") suspend fun deleteById(id: String)
}
