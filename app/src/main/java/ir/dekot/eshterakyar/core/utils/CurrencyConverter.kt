package ir.dekot.eshterakyar.core.utils

/**
 * انواع ارزهای پشتیبانی شده در برنامه
 * 
 * @property code کد ارز (مثل IRT, USD)
 * @property persianName نام فارسی ارز
 * @property symbol نماد ارز
 * @property rateToIRT نرخ تبدیل به تومان (پایه)
 */
enum class Currency(
    val code: String,
    val persianName: String,
    val symbol: String,
    val rateToIRT: Double
) {
    IRT("IRT", "تومان", "ت", 1.0),
    USD("USD", "دلار", "$", 60000.0),     // نرخ تقریبی: هر دلار ۶۰,۰۰۰ تومان
    EUR("EUR", "یورو", "€", 65000.0),      // نرخ تقریبی: هر یورو ۶۵,۰۰۰ تومان
    GBP("GBP", "پوند", "£", 76000.0),      // نرخ تقریبی: هر پوند ۷۶,۰۰۰ تومان
    AED("AED", "درهم", "د.إ", 16300.0);    // نرخ تقریبی: هر درهم ۱۶,۳۰۰ تومان

    companion object {
        /**
         * پیدا کردن ارز بر اساس کد
         * @param code کد ارز
         * @return ارز متناظر یا IRT به عنوان پیش‌فرض
         */
        fun fromCode(code: String): Currency {
            return entries.find { it.code == code } ?: IRT
        }
    }
}

/**
 * ابزار تبدیل ارزها
 * 
 * این کلاس از نرخ‌های ثابت استفاده می‌کند.
 * در نسخه‌های آینده می‌توان از API برای نرخ‌های زنده استفاده کرد.
 */
object CurrencyConverter {

    /**
     * تبدیل مبلغ از یک ارز به ارز دیگر
     * 
     * @param amount مبلغ اصلی
     * @param from ارز مبدأ
     * @param to ارز مقصد
     * @return مبلغ تبدیل شده
     */
    fun convert(amount: Double, from: Currency, to: Currency): Double {
        if (from == to) return amount
        
        // ابتدا به تومان تبدیل می‌کنیم، سپس به ارز مقصد
        val amountInIRT = amount * from.rateToIRT
        return amountInIRT / to.rateToIRT
    }

    /**
     * تبدیل مبلغ با استفاده از کد ارزها (String)
     * 
     * @param amount مبلغ اصلی
     * @param fromCode کد ارز مبدأ
     * @param toCode کد ارز مقصد
     * @return مبلغ تبدیل شده
     */
    fun convert(amount: Double, fromCode: String, toCode: String): Double {
        val from = Currency.fromCode(fromCode)
        val to = Currency.fromCode(toCode)
        return convert(amount, from, to)
    }

    /**
     * فرمت‌بندی قیمت با نماد ارز
     * 
     * @param amount مبلغ
     * @param currency ارز
     * @param showDecimals نمایش اعشار (پیش‌فرض: فقط برای ارزهای خارجی)
     * @return رشته فرمت شده قیمت
     */
    fun formatPrice(
        amount: Double,
        currency: Currency,
        showDecimals: Boolean = currency != Currency.IRT
    ): String {
        val formattedAmount = if (showDecimals) {
            String.format("%,.2f", amount)
        } else {
            String.format("%,.0f", amount)
        }
        
        // برای تومان، نماد را بعد از عدد قرار می‌دهیم (فرمت فارسی)
        return if (currency == Currency.IRT) {
            "$formattedAmount ${currency.persianName}"
        } else {
            "${currency.symbol}$formattedAmount"
        }
    }

    /**
     * فرمت‌بندی قیمت با استفاده از کد ارز (String)
     */
    fun formatPrice(amount: Double, currencyCode: String): String {
        val currency = Currency.fromCode(currencyCode)
        return formatPrice(amount, currency)
    }

    /**
     * محاسبه هزینه ماهانه نرمال‌سایز شده بر اساس دوره صورتحساب
     * 
     * @param price قیمت
     * @param billingCycleDays تعداد روزهای دوره
     * @return هزینه ماهانه (۳۰ روزه)
     */
    fun calculateMonthlyEquivalent(price: Double, billingCycleDays: Int): Double {
        return (price / billingCycleDays) * 30
    }
}
