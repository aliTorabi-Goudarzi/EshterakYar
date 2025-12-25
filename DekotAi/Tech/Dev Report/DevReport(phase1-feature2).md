# گزارش توسعه: فاز ۱ - ویژگی ۲ (داشبورد خلاصه وضعیت)

**تاریخ:** 2025-12-25
**توسعه‌دهنده:** Antigravity Agent

## 📋 خلاصه تغییرات
این ویژگی کارت آمار داشبورد (`StatsCard`) را کامل کرد با افزودن "نزدیک‌ترین تمدید" به کارت‌های موجود "تعداد اشتراک‌های فعال" و "هزینه ماهانه".

## 🛠️ جزئیات پیاده‌سازی

### ۱. لایه داده (Data Layer)
- **`SubscriptionDao.kt`**: کوئری جدید `getNearestRenewalDate()` برای بازیابی کمترین تاریخ تمدید.
- **`SubscriptionRepository.kt`**: متد جدید `getNearestRenewalDate(): Long?`.
- **`SubscriptionRepositoryImpl.kt`**: پیاده‌سازی متد فوق.

### ۲. لایه دامین (Domain Layer)
- **`SubscriptionStats`**: فیلد جدید `nearestRenewalDate: Date?`.
- **`GetSubscriptionStatsUseCase`**: فراخوانی `getNearestRenewalDate()` و تبدیل به `Date`.

### ۳. لایه نمایش (Presentation Layer)
- **`StatsCard.kt`**: افزودن `StatItem` سوم برای نمایش تاریخ نزدیک‌ترین تمدید به صورت شمسی.

## ✅ تایید
- **Build:** کامپایل موفق (`./gradlew :app:compileDebugKotlin`).

---
**فایل‌های ایجاد/ویرایش شده:**
- `core/database/SubscriptionDao.kt`
- `feature_addSubscription/domain/repository/SubscriptionRepository.kt`
- `feature_addSubscription/data/repository/SubscriptionRepositoryImpl.kt`
- `feature_addSubscription/domain/usecase/GetSubscriptionStatsUseCase.kt`
- `feature_home/presentation/components/StatsCard.kt`
