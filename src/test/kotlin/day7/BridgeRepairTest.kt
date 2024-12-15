package day7

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class BridgeRepairTest {
    private val sample1 = BridgeRepair("/day7/sample1")

    @Test
    fun sample1star1() {
        assertEquals(3749, sample1.star1())
    }

    @Test
    fun sample1star2() {
        assertEquals(11387, sample1.star2())
    }
}