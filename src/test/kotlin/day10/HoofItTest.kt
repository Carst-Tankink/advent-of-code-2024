package day10

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HoofItTest {
    private val sample1 = HoofIt("/day10/sample1")

    @Test
    fun sample1star1() {
        assertEquals(36, sample1.star1())
    }

    @Test
    fun sample2star1() {
        val sample2 = HoofIt("/day10/sample2")
        assertEquals(1, sample2.star1())
    }

    @Test
    fun sample1star2() {
        assertEquals(81, sample1.star2())
    }
}
