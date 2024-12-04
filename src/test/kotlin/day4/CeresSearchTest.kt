package day4

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CeresSearchTest {
    private val sample1 = CeresSearch("/day4/sample1")

    @Test
    fun sample1star1() {
        assertEquals(18, sample1.star1())
    }

    @Test
    fun sample1Star2() {
        assertEquals(9, sample1.star2())
    }
}