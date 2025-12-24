package ir.dekot.eshterakyar.core.domain.utils

import ir.dekot.eshterakyar.core.domain.model.JalaliDate
import java.time.LocalDate

object DateConverter {

    private val gregorianDaysInMonth = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    private val jalaliDaysInMonth = intArrayOf(31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29)

    fun toJalali(date: LocalDate): JalaliDate {
        return gregorianToJalali(date.year, date.monthValue, date.dayOfMonth)
    }

    fun toGregorian(date: JalaliDate): LocalDate {
        return jalaliToGregorian(date.year, date.month, date.day)
    }

    private fun gregorianToJalali(gy: Int, gm: Int, gd: Int): JalaliDate {
        val g_d_m = intArrayOf(0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334)
        var gy2 = if (gy > 1600) gy - 1600 else gy - 621
        var days =
                365 * gy2 + (gy2 + 3) / 4 - (gy2 + 99) / 100 + (gy2 + 399) / 400 - 80 +
                        gd +
                        g_d_m[gm - 1]

        // Leap year adjustment for Gregorian
        val gLeap = (gy % 4 == 0 && gy % 100 != 0) || (gy % 400 == 0)
        if (gLeap && gm > 2) days++

        var jy = 979 + 33 * (days / 12053)
        days %= 12053
        jy += 4 * (days / 1461)
        days %= 1461

        if (days > 365) {
            jy += (days - 1) / 365
            days = (days - 1) % 365
        }

        val jm = if (days < 186) 1 + days / 31 else 7 + (days - 186) / 30
        val jd = 1 + if (days < 186) days % 31 else (days - 186) % 30

        return JalaliDate(jy, jm, jd)
    }

    private fun jalaliToGregorian(jy: Int, jm: Int, jd: Int): LocalDate {
        val base = jy - 474
        val epy = 474 + base % 2820
        val md = if (jm <= 7) (jm - 1) * 31 else (jm - 1) * 30 + 6
        val jdn =
                jd +
                        md +
                        (epy * 682 - 110) / 2816 +
                        (epy - 1) * 365 +
                        (base / 2820) * 1029983 +
                        1948320
        return LocalDate.ofEpochDay(jdn.toLong() - 2440588)
    }
}
