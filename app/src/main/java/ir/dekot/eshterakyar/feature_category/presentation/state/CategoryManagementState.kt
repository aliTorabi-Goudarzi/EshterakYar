package ir.dekot.eshterakyar.feature_category.presentation.state

import ir.dekot.eshterakyar.feature_category.domain.model.CategoryItem

/** وضعیت UI صفحه مدیریت دسته‌بندی‌ها */
data class CategoryManagementState(
        val categories: List<CategoryItem> = emptyList(),
        val isLoading: Boolean = true,
        val showAddDialog: Boolean = false,
        val editingCategory: CategoryItem? = null,
        val newCategoryName: String = "",
        val selectedIconName: String = "category",
        val selectedColorCode: String = "#607D8B",
        val errorMessage: String? = null
)
