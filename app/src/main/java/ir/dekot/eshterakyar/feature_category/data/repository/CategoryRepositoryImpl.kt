package ir.dekot.eshterakyar.feature_category.data.repository

import ir.dekot.eshterakyar.feature_addSubscription.domain.model.SubscriptionCategory
import ir.dekot.eshterakyar.feature_category.data.datasource.CategoryDao
import ir.dekot.eshterakyar.feature_category.domain.model.CategoryItem
import ir.dekot.eshterakyar.feature_category.domain.model.CustomCategory
import ir.dekot.eshterakyar.feature_category.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** پیاده‌سازی ریپازیتوری دسته‌بندی‌ها ترکیب دسته‌های پیش‌فرض (enum) و سفارشی (Room) */
class CategoryRepositoryImpl(private val categoryDao: CategoryDao) : CategoryRepository {

    /** تبدیل enum پیش‌فرض به CategoryItem */
    private val defaultCategories: List<CategoryItem> =
            SubscriptionCategory.entries.map { category ->
                CategoryItem(
                        id = category.name,
                        name = category.persianName,
                        iconName = getCategoryIconName(category),
                        colorCode = getCategoryColor(category),
                        isDefault = true
                )
            }

    override fun getAllCategories(): Flow<List<CategoryItem>> {
        return categoryDao.getAllCustomCategories().map { customCategories ->
            val customItems = customCategories.map { it.toCategoryItem() }
            defaultCategories + customItems
        }
    }

    override suspend fun getAllCategoriesList(): List<CategoryItem> {
        val customCategories = categoryDao.getAllCustomCategoriesList()
        val customItems = customCategories.map { it.toCategoryItem() }
        return defaultCategories + customItems
    }

    override suspend fun getCategoryById(id: String): CategoryItem? {
        // اول در پیش‌فرض‌ها جستجو کن
        val defaultCategory = defaultCategories.find { it.id == id }
        if (defaultCategory != null) return defaultCategory

        // سپس در سفارشی‌ها
        return categoryDao.getCategoryById(id)?.toCategoryItem()
    }

    override suspend fun addCustomCategory(name: String, iconName: String, colorCode: String) {
        val category = CustomCategory(name = name, iconName = iconName, colorCode = colorCode)
        categoryDao.insert(category)
    }

    override suspend fun updateCategory(category: CustomCategory) {
        categoryDao.update(category)
    }

    override suspend fun deleteCategory(categoryId: String) {
        // فقط دسته‌های سفارشی قابل حذف هستند
        val isDefault = defaultCategories.any { it.id == categoryId }
        if (!isDefault) {
            categoryDao.deleteById(categoryId)
        }
    }

    /** تبدیل CustomCategory به CategoryItem */
    private fun CustomCategory.toCategoryItem(): CategoryItem =
            CategoryItem(
                    id = this.id,
                    name = this.name,
                    iconName = this.iconName,
                    colorCode = this.colorCode,
                    isDefault = false
            )

    /** دریافت نام آیکون برای دسته پیش‌فرض */
    private fun getCategoryIconName(category: SubscriptionCategory): String =
            when (category) {
                SubscriptionCategory.ENTERTAINMENT -> "movie"
                SubscriptionCategory.PRODUCTIVITY -> "work"
                SubscriptionCategory.EDUCATION -> "school"
                SubscriptionCategory.HEALTH -> "fitness_center"
                SubscriptionCategory.NEWS -> "newspaper"
                SubscriptionCategory.MUSIC -> "music_note"
                SubscriptionCategory.VIDEO -> "videocam"
                SubscriptionCategory.CLOUD -> "cloud"
                SubscriptionCategory.OTHER -> "category"
            }

    /** دریافت رنگ برای دسته پیش‌فرض */
    private fun getCategoryColor(category: SubscriptionCategory): String =
            when (category) {
                SubscriptionCategory.ENTERTAINMENT -> "#E91E63"
                SubscriptionCategory.PRODUCTIVITY -> "#2196F3"
                SubscriptionCategory.EDUCATION -> "#4CAF50"
                SubscriptionCategory.HEALTH -> "#FF5722"
                SubscriptionCategory.NEWS -> "#9C27B0"
                SubscriptionCategory.MUSIC -> "#00BCD4"
                SubscriptionCategory.VIDEO -> "#F44336"
                SubscriptionCategory.CLOUD -> "#3F51B5"
                SubscriptionCategory.OTHER -> "#607D8B"
            }
}
