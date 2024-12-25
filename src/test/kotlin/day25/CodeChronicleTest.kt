package day25

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CodeChronicleTest {
    private val sample1 = CodeChronicle("/day25/sample1")

    @Test
    fun sample1star1() {
        assertEquals(3, sample1.star1())
    }
}