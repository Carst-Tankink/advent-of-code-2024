package day21

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class KeypadConundrumTest {
    private val sample1 = KeypadConundrum("/day21/sample1")

    @Test
    fun sample1star1() {
        assertEquals(126384, sample1.star1())
    }
}