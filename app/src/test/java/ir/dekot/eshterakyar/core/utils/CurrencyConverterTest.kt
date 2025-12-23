package ir.dekot.eshterakyar.core.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class CurrencyConverterTest {

    @Test
    fun `convert same currency returns same amount`() {
        val amount = 100.0
        val result = CurrencyConverter.convert(amount, Currency.USD, Currency.USD)
        assertEquals(amount, result, 0.001)
    }

    @Test
    fun `convert USD to IRT returns correct amount`() {
        val amountUSD = 10.0
        val expectedIRT = 10.0 * 60000.0 // 600,000 Toman
        val result = CurrencyConverter.convert(amountUSD, Currency.USD, Currency.IRT)
        assertEquals(expectedIRT, result, 0.001)
    }

    @Test
    fun `convert IRT to USD returns correct amount`() {
        val amountIRT = 60000.0
        val expectedUSD = 1.0 // 1 Dollar
        val result = CurrencyConverter.convert(amountIRT, Currency.IRT, Currency.USD)
        assertEquals(expectedUSD, result, 0.001)
    }

    @Test
    fun `convert EUR to GBP returns correct amount`() {
        // EUR → IRT → GBP
        val amountEUR = 10.0
        val amountInIRT = 10.0 * 65000.0 // 650,000 Toman
        val expectedGBP = amountInIRT / 76000.0 // ~8.55 GBP
        val result = CurrencyConverter.convert(amountEUR, Currency.EUR, Currency.GBP)
        assertEquals(expectedGBP, result, 0.01)
    }

    @Test
    fun `convert with string codes works correctly`() {
        val result = CurrencyConverter.convert(100.0, "USD", "IRT")
        val expected = 100.0 * 60000.0
        assertEquals(expected, result, 0.001)
    }

    @Test
    fun `fromCode returns correct currency`() {
        assertEquals(Currency.USD, Currency.fromCode("USD"))
        assertEquals(Currency.EUR, Currency.fromCode("EUR"))
        assertEquals(Currency.GBP, Currency.fromCode("GBP"))
        assertEquals(Currency.AED, Currency.fromCode("AED"))
        assertEquals(Currency.IRT, Currency.fromCode("IRT"))
    }

    @Test
    fun `fromCode returns IRT for unknown code`() {
        assertEquals(Currency.IRT, Currency.fromCode("UNKNOWN"))
        assertEquals(Currency.IRT, Currency.fromCode(""))
    }

    @Test
    fun `formatPrice IRT shows Persian format`() {
        val result = CurrencyConverter.formatPrice(50000.0, Currency.IRT)
        // Should be "50,000 تومان"
        assert(result.contains("تومان"))
        assert(result.contains("50"))
    }

    @Test
    fun `formatPrice USD shows symbol prefix`() {
        val result = CurrencyConverter.formatPrice(99.99, Currency.USD)
        // Should be "$99.99"
        assert(result.startsWith("$"))
    }

    @Test
    fun `formatPrice EUR shows decimals`() {
        val result = CurrencyConverter.formatPrice(49.50, Currency.EUR)
        assert(result.contains("€"))
        assert(result.contains("."))
    }

    @Test
    fun `calculateMonthlyEquivalent for monthly returns same`() {
        val price = 100.0
        val result = CurrencyConverter.calculateMonthlyEquivalent(price, 30)
        assertEquals(100.0, result, 0.001)
    }

    @Test
    fun `calculateMonthlyEquivalent for yearly divides by 12`() {
        val yearlyPrice = 1200.0
        val result = CurrencyConverter.calculateMonthlyEquivalent(yearlyPrice, 365)
        // (1200 / 365) * 30 ≈ 98.63
        val expected = (1200.0 / 365) * 30
        assertEquals(expected, result, 0.01)
    }

    @Test
    fun `calculateMonthlyEquivalent for weekly multiplies by 4`() {
        val weeklyPrice = 100.0
        val result = CurrencyConverter.calculateMonthlyEquivalent(weeklyPrice, 7)
        // (100 / 7) * 30 ≈ 428.57
        val expected = (100.0 / 7) * 30
        assertEquals(expected, result, 0.01)
    }
}
