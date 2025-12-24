package ir.dekot.eshterakyar.core.domain.model

/**
 * A data class representing a Jalali (Persian) date.
 *
 * @property year The year (e.g., 1403)
 * @property month The month (1-12)
 * @property day The day of the month (1-31)
 */
data class JalaliDate(val year: Int, val month: Int, val day: Int) : Comparable<JalaliDate> {
    init {
        require(year > 0) { "Year must be positive" }
        require(month in 1..12) { "Month must be between 1 and 12" }
        require(day >= 1) { "Day must be at least 1" }

        val maxDays = monthLength(year, month)
        require(day <= maxDays) {
            "Day $day is invalid for month $month in year $year (max $maxDays)"
        }
    }

    override fun compareTo(other: JalaliDate): Int {
        if (year != other.year) return year - other.year
        if (month != other.month) return month - other.month
        return day - other.day
    }

    override fun toString(): String {
        return "$year/${month.toString().padStart(2, '0')}/${day.toString().padStart(2, '0')}"
    }

    fun format(separator: String = "/"): String {
        return "$year$separator${month.toString().padStart(2, '0')}$separator${day.toString().padStart(2, '0')}"
    }

    fun monthName(): String {
        return when (month) {
            1 -> "فروردین"
            2 -> "اردیبهشت"
            3 -> "خرداد"
            4 -> "تیر"
            5 -> "مرداد"
            6 -> "شهریور"
            7 -> "مهر"
            8 -> "آبان"
            9 -> "آذر"
            10 -> "دی"
            11 -> "بهمن"
            12 -> "اسفند"
            else -> ""
        }
    }

    /**
     * Checks if this year is a leap year. Uses the 33-year cycle algorithm which is accurate for
     * the current era.
     */
    val isLeap: Boolean
        get() = isLeapYear(year)

    /** Returns the length of the month for this date. */
    val monthLength: Int
        get() = monthLength(year, month)

    companion object {
        /** Checks if a Jalali year is a leap year. */
        fun isLeapYear(year: Int): Boolean {
            val r = year % 33
            return r == 1 || r == 5 || r == 9 || r == 13 || r == 17 || r == 22 || r == 26 || r == 30
        }

        /** Returns the number of days in a given Jalali month. */
        fun monthLength(year: Int, month: Int): Int {
            return when (month) {
                in 1..6 -> 31
                in 7..11 -> 30
                12 -> if (isLeapYear(year)) 30 else 29
                else -> throw IllegalArgumentException("Invalid month: $month")
            }
        }
    }
}
