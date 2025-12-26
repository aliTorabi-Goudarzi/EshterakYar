package ir.dekot.eshterakyar.feature_category.domain.usecase

import ir.dekot.eshterakyar.feature_category.domain.model.CustomCategory
import ir.dekot.eshterakyar.feature_category.domain.repository.CategoryRepository

/** یوزکیس به‌روزرسانی دسته‌بندی سفارشی */
class UpdateCategoryUseCase(private val repository: CategoryRepository) {
    /**
     * به‌روزرسانی دسته سفارشی
     *
     * @param category دسته با اطلاعات جدید
     */
    suspend operator fun invoke(category: CustomCategory) {
        require(category.name.isNotBlank()) { "نام دسته نمی‌تواند خالی باشد" }
        repository.updateCategory(category)
    }
}
