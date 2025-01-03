package day20

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RaceConditionTest {
    private val sample1 = RaceCondition("/day20/sample1")

    @Test
    fun sample1star1() {
        assertEquals(0, sample1.star1())
    }
}