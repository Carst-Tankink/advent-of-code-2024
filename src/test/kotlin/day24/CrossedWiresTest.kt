package day24

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CrossedWiresTest {

    private val sample1 = CrossedWires("/day24/sample1")
    private val sample2 = CrossedWires("/day24/sample2")

    @Test
    fun sample1star1() {
        assertEquals(4, sample1.star1())
    }

    @Test
    fun sample2star1() {
        assertEquals(2024, sample2.star1())
    }
}