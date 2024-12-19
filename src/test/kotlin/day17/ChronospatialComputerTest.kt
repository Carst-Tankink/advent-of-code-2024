package day17

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ChronospatialComputerTest {
    private val sample1 = ChronospatialComputer("/day17/sample1")

    @Test
    fun sample1star1() {
        assertEquals("4,6,3,5,6,3,5,2,1,0", sample1.star1())
    }

    @Test
    fun sample1star2() {
        assertEquals("117440", sample1.star2())
    }
}