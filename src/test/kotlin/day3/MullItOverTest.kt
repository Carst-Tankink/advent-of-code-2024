package day3

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MullItOverTest {

    private val sample1 = MullItOver(fileName = "/day3/sample1")
    private val sample2 = MullItOver(fileName = "/day3/sample2")

    @Test
    fun sample1star1() {
        assertEquals(161, sample1.star1())
    }

    @Test
    fun sample2star2() {
        assertEquals(48, sample2.star2())
    }
}