package ir.dekot.eshterakyar.feature_category.domain.repository

import ir.dekot.eshterakyar.feature_category.domain.model.CategoryItem
import ir.dekot.eshterakyar.feature_category.domain.model.CustomCategory
import kotlinx.coroutines.flow.Flow

/** رابط ریپازیتوری برای مدیریت دسته‌بندی‌ها */
interface CategoryRepository {
    /** دریافت همه دسته‌بندی‌ها (پیش‌فرض + سفارشی) */
    fun getAllCategories(): Flow<List<CategoryItem>>

    /** دریافت لیست همه دسته‌بندی‌ها به صورت همزمان */
    suspend fun getAllCategoriesList(): List<CategoryItem>

    /** دریافت دسته با شناسه */
    suspend fun getCategoryById(id: String): CategoryItem?

    /** افزودن دسته سفارشی جدید */
    suspend fun addCustomCategory(name: String, iconName: String, colorCode: String)

    /** به‌روزرسانی دسته سفارشی */
    suspend fun updateCategory(category: CustomCategory)

    /** حذف دسته سفارشی */
    suspend fun deleteCategory(categoryId: String)
}
