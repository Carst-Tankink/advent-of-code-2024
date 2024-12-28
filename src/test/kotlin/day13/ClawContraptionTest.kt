package day13

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ClawContraptionTest {
    private val sample1 = ClawContraption("/day13/sample1")

    @Test
    fun sample1star1() {
        assertEquals(480, sample1.star1())
    }
}
