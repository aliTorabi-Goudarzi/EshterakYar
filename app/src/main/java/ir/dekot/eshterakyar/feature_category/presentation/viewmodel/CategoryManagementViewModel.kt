package ir.dekot.eshterakyar.feature_category.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.dekot.eshterakyar.feature_category.domain.model.CategoryItem
import ir.dekot.eshterakyar.feature_category.domain.model.CustomCategory
import ir.dekot.eshterakyar.feature_category.domain.usecase.AddCategoryUseCase
import ir.dekot.eshterakyar.feature_category.domain.usecase.DeleteCategoryUseCase
import ir.dekot.eshterakyar.feature_category.domain.usecase.GetAllCategoriesUseCase
import ir.dekot.eshterakyar.feature_category.domain.usecase.UpdateCategoryUseCase
import ir.dekot.eshterakyar.feature_category.presentation.intent.CategoryManagementIntent
import ir.dekot.eshterakyar.feature_category.presentation.state.CategoryManagementState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** ویومدل مدیریت دسته‌بندی‌ها */
class CategoryManagementViewModel(
        private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
        private val addCategoryUseCase: AddCategoryUseCase,
        private val deleteCategoryUseCase: DeleteCategoryUseCase,
        private val updateCategoryUseCase: UpdateCategoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryManagementState())
    val uiState: StateFlow<CategoryManagementState> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    /** بارگذاری دسته‌بندی‌ها */
    private fun loadCategories() {
        viewModelScope.launch {
            getAllCategoriesUseCase().collectLatest { categories ->
                _uiState.update { it.copy(categories = categories, isLoading = false) }
            }
        }
    }

    /** پردازش اکشن‌های کاربر */
    fun processIntent(intent: CategoryManagementIntent) {
        when (intent) {
            is CategoryManagementIntent.OnShowAddDialog -> showAddDialog()
            is CategoryManagementIntent.OnDismissDialog -> dismissDialog()
            is CategoryManagementIntent.OnNameChanged -> updateName(intent.name)
            is CategoryManagementIntent.OnIconChanged -> updateIcon(intent.iconName)
            is CategoryManagementIntent.OnColorChanged -> updateColor(intent.colorCode)
            is CategoryManagementIntent.OnSaveCategory -> saveCategory()
            is CategoryManagementIntent.OnEditCategory -> editCategory(intent.category)
            is CategoryManagementIntent.OnDeleteCategory -> deleteCategory(intent.categoryId)
            is CategoryManagementIntent.OnClearError -> clearError()
        }
    }

    private fun showAddDialog() {
        _uiState.update {
            it.copy(
                    showAddDialog = true,
                    editingCategory = null,
                    newCategoryName = "",
                    selectedIconName = "category",
                    selectedColorCode = "#607D8B"
            )
        }
    }

    private fun dismissDialog() {
        _uiState.update {
            it.copy(
                    showAddDialog = false,
                    editingCategory = null,
                    newCategoryName = "",
                    errorMessage = null
            )
        }
    }

    private fun updateName(name: String) {
        _uiState.update { it.copy(newCategoryName = name) }
    }

    private fun updateIcon(iconName: String) {
        _uiState.update { it.copy(selectedIconName = iconName) }
    }

    private fun updateColor(colorCode: String) {
        _uiState.update { it.copy(selectedColorCode = colorCode) }
    }

    private fun saveCategory() {
        val state = _uiState.value
        val name = state.newCategoryName.trim()

        if (name.isBlank()) {
            _uiState.update { it.copy(errorMessage = "نام دسته نمی‌تواند خالی باشد") }
            return
        }

        viewModelScope.launch {
            try {
                val editing = state.editingCategory
                if (editing != null && !editing.isDefault) {
                    // به‌روزرسانی دسته موجود
                    updateCategoryUseCase(
                            CustomCategory(
                                    id = editing.id,
                                    name = name,
                                    iconName = state.selectedIconName,
                                    colorCode = state.selectedColorCode
                            )
                    )
                } else {
                    // افزودن دسته جدید
                    addCategoryUseCase(
                            name = name,
                            iconName = state.selectedIconName,
                            colorCode = state.selectedColorCode
                    )
                }
                dismissDialog()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "خطا در ذخیره دسته") }
            }
        }
    }

    private fun editCategory(category: CategoryItem) {
        if (category.isDefault) {
            _uiState.update { it.copy(errorMessage = "دسته‌های پیش‌فرض قابل ویرایش نیستند") }
            return
        }
        _uiState.update {
            it.copy(
                    showAddDialog = true,
                    editingCategory = category,
                    newCategoryName = category.name,
                    selectedIconName = category.iconName,
                    selectedColorCode = category.colorCode
            )
        }
    }

    private fun deleteCategory(categoryId: String) {
        val category = _uiState.value.categories.find { it.id == categoryId }
        if (category?.isDefault == true) {
            _uiState.update { it.copy(errorMessage = "دسته‌های پیش‌فرض قابل حذف نیستند") }
            return
        }

        viewModelScope.launch {
            try {
                deleteCategoryUseCase(categoryId)
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "خطا در حذف دسته") }
            }
        }
    }

    private fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
