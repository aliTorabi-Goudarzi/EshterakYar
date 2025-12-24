package ir.dekot.eshterakyar.core.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class JalaliDateTest {

    @Test
    fun `should create valid date successfully`() {
        val date = JalaliDate(1403, 1, 1)
        assertEquals(1403, date.year)
        assertEquals(1, date.month)
        assertEquals(1, date.day)
    }

    @Test
    fun `should throw exception for invalid month`() {
        assertThrows(IllegalArgumentException::class.java) {
            JalaliDate(1403, 13, 1)
        }
        assertThrows(IllegalArgumentException::class.java) {
            JalaliDate(1403, 0, 1)
        }
    }

    @Test
    fun `should throw exception for invalid day`() {
        assertThrows(IllegalArgumentException::class.java) {
            JalaliDate(1403, 1, 32)
        }
        assertThrows(IllegalArgumentException::class.java) {
            JalaliDate(1403, 1, 0)
        }
    }

    @Test
    fun `first 6 months should have 31 days`() {
        val date = JalaliDate(1403, 1, 31)
        assertEquals(31, date.monthLength)
    }

    @Test
    fun `second 6 months should have 30 days usually`() {
        val date = JalaliDate(1403, 7, 30)
        assertEquals(30, date.monthLength)
    }
    
    @Test
    fun `should identify leap years correctly`() {
        // 1403 is a leap year
        val date = JalaliDate(1403, 12, 30) // This should be valid in leap year
        assertTrue(date.isLeap)
        assertEquals(30, date.monthLength)
    }

    @Test
    fun `should identify non-leap years correctly`() {
        // 1402 was not a leap year
        val date = JalaliDate(1402, 1, 1)
        assertFalse(date.isLeap)
        
        // Month 12 in non-leap year has 29 days
        val dateEndOfYear = JalaliDate(1402, 12, 29)
        assertEquals(29, dateEndOfYear.monthLength)
    }

    @Test
    fun `should throw exception for day 30 in Esfand of non-leap year`() {
        assertThrows(IllegalArgumentException::class.java) {
            JalaliDate(1402, 12, 30)
        }
    }
}
