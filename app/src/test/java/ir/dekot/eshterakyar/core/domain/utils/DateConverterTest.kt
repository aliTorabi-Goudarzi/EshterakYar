package ir.dekot.eshterakyar.core.domain.utils

import ir.dekot.eshterakyar.core.domain.model.JalaliDate
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test

class DateConverterTest {

    @Test
    fun `should convert Gregorian to Jalali correctly`() {
        // 2024-03-20 is 1403-01-01 (Norouz)
        val gregorian = LocalDate.of(2024, 3, 20)
        val jalali = DateConverter.toJalali(gregorian)

        assertEquals(1403, jalali.year)
        assertEquals(1, jalali.month)
        assertEquals(1, jalali.day)
    }

    @Test
    fun `should convert Jalali to Gregorian correctly`() {
        val jalali = JalaliDate(1403, 1, 1)
        val gregorian = DateConverter.toGregorian(jalali)

        assertEquals(2024, gregorian.year)
        assertEquals(3, gregorian.monthValue)
        assertEquals(20, gregorian.dayOfMonth)
    }

    @Test
    fun `should handle leap years correctly in conversion`() {
        // 2025-03-20 is 1403-12-30 (end of leap year 1403)
        // Wait, 1403 is leap. so 1403-12-30 exists.
        // Let's verify specific date.
        // 2025-03-20 -> 1403-12-30

        val jalali = JalaliDate(1403, 12, 30)
        val gregorian = DateConverter.toGregorian(jalali)
        assertEquals(LocalDate.of(2025, 3, 20), gregorian)

        val backToJalali = DateConverter.toJalali(gregorian)
        assertEquals(jalali, backToJalali)
    }
}
