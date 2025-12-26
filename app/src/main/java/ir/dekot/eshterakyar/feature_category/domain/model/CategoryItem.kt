package ir.dekot.eshterakyar.feature_category.domain.model

/**
 * مدل یکپارچه برای نمایش دسته‌بندی (هم پیش‌فرض و هم سفارشی)
 *
 * @property id شناسه یکتا (enum.name برای پیش‌فرض یا UUID برای سفارشی)
 * @property name نام فارسی دسته
 * @property iconName نام آیکون
 * @property colorCode کد رنگ هگز
 * @property isDefault آیا دسته پیش‌فرض است (غیرقابل حذف)
 */
data class CategoryItem(
        val id: String,
        val name: String,
        val iconName: String,
        val colorCode: String,
        val isDefault: Boolean
)
