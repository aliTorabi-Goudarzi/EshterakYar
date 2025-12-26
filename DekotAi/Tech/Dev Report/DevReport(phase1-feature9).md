# 🧾 Development Report

---

## 📌 Metadata

- **Project Name:** EshterakYar (اشتراک‌یار)
- **Phase:** Phase 1
- **Feature:** Feature 9 — پشتیبانی از ریال و تومان
- **Implementation Date:** 2025-12-26
- **Agent Mode:** IMPLEMENT

---

## 🎯 Feature Summary

افزودن ریال (IRR) به سیستم ارز برنامه و تنظیمات واحد پول.

---

## ✏️ Files Modified

| فایل | تغییر |
|------|-------|
| `CurrencyConverter.kt` | افزودن IRR به enum و آپدیت `formatPrice()` |
| `SubscriptionCard.kt` | استفاده از `CurrencyConverter.formatPrice()` |

---

## 📝 Notes

- سیستم ارز `CurrencyPreferences` و `CurrencySelector` قبلاً وجود داشت
- فقط IRR (ریال) به enum اضافه شد: `rate = 0.1` (هر ۱۰ ریال = ۱ تومان)
- `SubscriptionCard` از formatPrice محلی به `CurrencyConverter.formatPrice()` تغییر یافت

---

## ✅ Completion Checklist

- [x] IRR added to Currency enum
- [x] formatPrice handles both IRT and IRR
- [x] SubscriptionCard uses CurrencyConverter
- [x] Build successful
- [x] Roadmap updated
