package day1

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HistorianHysteriaTest {
    private val day1Folder = "/day1"

    private val sample1 = HistorianHysteria("$day1Folder/sample1")

    @Test
    fun sampleStar1() {
        assertEquals(11, sample1.star1())
    }

    @Test
    fun sample1Star2() {
        assertEquals(31, sample1.star2())
    }
}