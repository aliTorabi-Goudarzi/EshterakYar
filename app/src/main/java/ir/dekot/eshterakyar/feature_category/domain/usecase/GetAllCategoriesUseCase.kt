package ir.dekot.eshterakyar.feature_category.domain.usecase

import ir.dekot.eshterakyar.feature_category.domain.model.CategoryItem
import ir.dekot.eshterakyar.feature_category.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow

/** یوزکیس دریافت همه دسته‌بندی‌ها */
class GetAllCategoriesUseCase(private val repository: CategoryRepository) {
    /** دریافت جریان همه دسته‌بندی‌ها */
    operator fun invoke(): Flow<List<CategoryItem>> {
        return repository.getAllCategories()
    }

    /** دریافت لیست همه دسته‌بندی‌ها */
    suspend fun getList(): List<CategoryItem> {
        return repository.getAllCategoriesList()
    }
}
