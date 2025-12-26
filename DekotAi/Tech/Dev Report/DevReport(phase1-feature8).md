# 🧾 Development Report

---

## 📌 Metadata

- **Project Name:** EshterakYar (اشتراک‌یار)
- **Phase:** Phase 1
- **Feature:** Feature 8 — مرتب‌سازی لیست
- **Implementation Date:** 2025-12-26
- **Agent Mode:** IMPLEMENT

---

## 🎯 Feature Summary

مرتب‌سازی لیست اشتراک‌ها با ۶ گزینه:
- نزدیک‌ترین تمدید / دورترین تمدید
- گران‌ترین / ارزان‌ترین
- نام (الف تا ی) / نام (ی تا الف)

---

## ✏️ Files Modified

| فایل | تغییر |
|------|-------|
| `HomeContract.kt` | افزودن `SortOption` enum و `selectedSortOption` به State |
| `HomeViewModel.kt` | افزودن `changeSortOption()` و `applyFilterAndSort()` |
| `HomeScreen.kt` | افزودن FilterChip dropdown برای انتخاب نوع مرتب‌سازی |

---

## ✅ Completion Checklist

- [x] Feature fully implemented
- [x] In-memory sorting (no database changes needed)
- [x] Build successful
- [x] Roadmap updated
