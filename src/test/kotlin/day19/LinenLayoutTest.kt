package day19

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LinenLayoutTest {
    private val sample1 = LinenLayout("/day19/sample1")

    @Test
    fun sample1star1() {
        assertEquals(6, sample1.star1())
    }

    @Test
    fun sample1star2() {
        assertEquals(16, sample1.star2())
    }
}