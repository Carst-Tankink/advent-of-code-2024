package day12

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GardenGroupsTest {
    private val sample1 = GardenGroups("/day12/sample1")
    private val sample2 = GardenGroups("/day12/sample2")
    private val sample3 = GardenGroups("/day12/sample3")
    private val sample4 = GardenGroups("/day12/sample4")
    private val sample5 = GardenGroups("/day12/sample5")

    @Test
    fun sample1star1() {
        assertEquals(140, sample1.star1())
    }

    @Test
    fun sample2star1() {
        assertEquals(772, sample2.star1())
    }

    @Test
    fun sample3star1() {
        assertEquals(1930, sample3.star1())
    }

    @Test
    fun sample1star2() {
        assertEquals(80, sample1.star2())
    }

    @Test
    fun sample2star2() {
        assertEquals(436, sample2.star2())
    }

    @Test
    fun sample3star2() {
        assertEquals(1206, sample3.star2())
    }

    @Test
    fun sample4star2() {
        assertEquals(236, sample4.star2())
    }

    @Test
    fun sample5star2() {
        assertEquals(368, sample5.star2())
    }
}