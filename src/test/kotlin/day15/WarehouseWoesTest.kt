package day15

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class WarehouseWoesTest {
    private val sample1 = WarehouseWoes("/day15/sample1")
    private val sample2 = WarehouseWoes("/day15/sample2")

    @Test
    fun sample1star1() {
        assertEquals(10092, sample1.star1())
    }

    @Test
    fun sample2star1() {
        assertEquals(2028, sample2.star1())
    }
}

