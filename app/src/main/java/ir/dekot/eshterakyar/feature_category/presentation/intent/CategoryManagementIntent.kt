package ir.dekot.eshterakyar.feature_category.presentation.intent

import ir.dekot.eshterakyar.feature_category.domain.model.CategoryItem

/** اکشن‌های کاربر در صفحه مدیریت دسته‌بندی */
sealed class CategoryManagementIntent {
    /** نمایش دیالوگ افزودن دسته */
    data object OnShowAddDialog : CategoryManagementIntent()

    /** بستن دیالوگ */
    data object OnDismissDialog : CategoryManagementIntent()

    /** تغییر نام دسته در دیالوگ */
    data class OnNameChanged(val name: String) : CategoryManagementIntent()

    /** تغییر آیکون دسته */
    data class OnIconChanged(val iconName: String) : CategoryManagementIntent()

    /** تغییر رنگ دسته */
    data class OnColorChanged(val colorCode: String) : CategoryManagementIntent()

    /** افزودن/ذخیره دسته */
    data object OnSaveCategory : CategoryManagementIntent()

    /** شروع ویرایش دسته */
    data class OnEditCategory(val category: CategoryItem) : CategoryManagementIntent()

    /** حذف دسته */
    data class OnDeleteCategory(val categoryId: String) : CategoryManagementIntent()

    /** پاک کردن پیام خطا */
    data object OnClearError : CategoryManagementIntent()
}
