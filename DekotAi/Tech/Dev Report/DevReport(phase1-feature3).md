# گزارش توسعه: فاز ۱ - ویژگی ۳ (پریست‌های سرویس‌های محبوب)

**تاریخ:** 2025-12-25
**توسعه‌دهنده:** Antigravity Agent

## 📋 خلاصه تغییرات
این ویژگی لیستی آماده از سرویس‌های محبوب ایرانی و بین‌المللی (فیلیمو، نماوا، اسنپ، دیجی‌پلاس، اسپاتیفای و...) را با لوگو و رنگ برند به صفحه افزودن اشتراک اضافه می‌کند. با انتخاب هر پریست، فیلدهای فرم به صورت خودکار پر می‌شوند.

## 🛠️ جزئیات پیاده‌سازی

### ۱. لایه دامین (Domain Layer)
- **`ServicePreset.kt`**: مدل پریست با `id`, `name`, `iconResId`, `colorCode`, `defaultCategory`, `defaultPrice`, `defaultBillingCycle`.
- **`ServicePresetRepository.kt`**: Interface برای دریافت لیست پریست‌ها.
- **`GetServicePresetsUseCase.kt`**: UseCase با `operator fun invoke(): List<ServicePreset>`.

### ۲. لایه داده (Data Layer)
- **`ServicePresetRepositoryImpl.kt`**: ۱۰ پریست استاتیک:
  - **ایرانی:** فیلیمو، نماوا، تلوبیون، اسنپ، تپسی، دیجی‌پلاس
  - **بین‌المللی:** اسپاتیفای، یوتیوب پریمیوم، نتفلیکس، گوگل وان

### ۳. لایه نمایش (Presentation Layer)
- **`AddSubscriptionUiState.kt`**: فیلدهای `servicePresets` و `selectedPreset`.
- **`AddSubscriptionIntent.kt`**: اینتنت‌های `OnPresetSelected` و `OnPresetCleared`.
- **`ServicePresetSelector.kt`**: کامپوننت افقی اسکرول‌پذیر با کارت‌های دایره‌ای.
- **`BasicInfoStep.kt`**: ادغام `ServicePresetSelector` در Step 1.
- **`AddSubscriptionViewModel.kt`**: لود پریست‌ها و پر کردن خودکار فیلدها هنگام انتخاب.

### ۴. منابع (Resources)
- **آیکون‌ها:** ۱۰ فایل vector در `res/drawable/` برای هر سرویس.
- **رشته‌ها:** `popular_services`, `or_add_manually` در `strings.xml`.

### ۵. DI
- **`AppModule.kt`**: ثبت `ServicePresetRepository` و `GetServicePresetsUseCase`.

## ✅ تایید
- **Build:** کامپایل موفق
- **Tests:** ۹ تست پاس شد (۳ تست UseCase + ۶ تست ViewModel)

---
**فایل‌های ایجاد شده:**
- `feature_addSubscription/domain/model/ServicePreset.kt`
- `feature_addSubscription/domain/repository/ServicePresetRepository.kt`
- `feature_addSubscription/domain/usecase/GetServicePresetsUseCase.kt`
- `feature_addSubscription/data/repository/ServicePresetRepositoryImpl.kt`
- `feature_addSubscription/presentation/component/ServicePresetSelector.kt`
- `res/drawable/ic_preset_*.xml` (10 files)

**فایل‌های ویرایش شده:**
- `feature_addSubscription/presentation/state/AddSubscriptionUiState.kt`
- `feature_addSubscription/presentation/intent/AddSubscriptionIntent.kt`
- `feature_addSubscription/presentation/viewmodel/AddSubscriptionViewModel.kt`
- `feature_addSubscription/presentation/component/BasicInfoStep.kt`
- `screens/AddSubscriptionScreen.kt`
- `core/di/AppModule.kt`
- `res/values/strings.xml`

**فایل‌های تست:**
- `GetServicePresetsUseCaseTest.kt` (جدید)
- `AddSubscriptionViewModelTest.kt` (به‌روزرسانی)
