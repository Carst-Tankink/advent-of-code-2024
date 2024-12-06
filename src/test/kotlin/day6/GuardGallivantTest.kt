package day6

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GuardGallivantTest {
    private val sample1 = GuardGallivant("/day6/sample1")

    @Test
    fun sample1star1() {
        assertEquals(41, sample1.star1())
    }

    @Test
    fun sample1star2() {
        assertEquals(6, sample1.star2())
    }
}