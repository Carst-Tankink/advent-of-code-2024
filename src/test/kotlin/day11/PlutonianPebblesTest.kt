package day11

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PlutonianPebblesTest {
    private val sample1 = PlutonianPebbles("/day11/sample1")

    @Test
    fun sample1star1() {
        assertEquals(55312L, sample1.star1())
    }
}