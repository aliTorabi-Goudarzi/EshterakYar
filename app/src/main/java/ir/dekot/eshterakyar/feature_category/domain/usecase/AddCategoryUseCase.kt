package ir.dekot.eshterakyar.feature_category.domain.usecase

import ir.dekot.eshterakyar.feature_category.domain.repository.CategoryRepository

/** یوزکیس افزودن دسته‌بندی سفارشی جدید */
class AddCategoryUseCase(private val repository: CategoryRepository) {
    /**
     * افزودن دسته جدید
     *
     * @param name نام دسته
     * @param iconName نام آیکون
     * @param colorCode کد رنگ
     */
    suspend operator fun invoke(
            name: String,
            iconName: String = "category",
            colorCode: String = "#607D8B"
    ) {
        require(name.isNotBlank()) { "نام دسته نمی‌تواند خالی باشد" }
        repository.addCustomCategory(name, iconName, colorCode)
    }
}
