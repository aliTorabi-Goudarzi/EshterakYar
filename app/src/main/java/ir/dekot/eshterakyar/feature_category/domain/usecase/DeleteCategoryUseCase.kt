package ir.dekot.eshterakyar.feature_category.domain.usecase

import ir.dekot.eshterakyar.feature_category.domain.repository.CategoryRepository

/** یوزکیس حذف دسته‌بندی سفارشی */
class DeleteCategoryUseCase(private val repository: CategoryRepository) {
    /**
     * حذف دسته سفارشی توجه: دسته‌های پیش‌فرض قابل حذف نیستند
     *
     * @param categoryId شناسه دسته
     */
    suspend operator fun invoke(categoryId: String) {
        repository.deleteCategory(categoryId)
    }
}
