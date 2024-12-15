package day8

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ResonantCollinearityTest {
    private val sample1 = ResonantCollinearity("/day8/sample1")

    @Test
    fun sample1star1() {
        assertEquals(14, sample1.star1())
    }

    @Test
    fun sample1star2() {
        assertEquals(34, sample1.star2())
    }
}