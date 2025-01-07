package day22

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MonkeyMarketTest {
    private val sample1 = MonkeyMarket("/day22/sample1")
    private val sample2 = MonkeyMarket("/day22/sample2")

    @Test
    fun sample1star1() {
        assertEquals(37327623, sample1.star1())
    }

    @Test
    fun sample2star2() {
        assertEquals(23, sample2.star2())
    }
}