package day14

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RestroomRedoubtTest {
    private val sample1 = RestroomRedoubt("/day14/sample1")

    @Test
    fun sample1star1() {
        assertEquals(12, sample1.calculateSafetyFactor(11, 7, 100))
    }
}