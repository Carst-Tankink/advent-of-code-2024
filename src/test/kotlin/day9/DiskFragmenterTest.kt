package day9

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DiskFragmenterTest {
    private val sample1 = DiskFragmenter("/day9/sample1")

    @Test
    fun sample1star1() {
        assertEquals(1928, sample1.star1())
    }

    @Test
    fun sample1star2() {
        assertEquals(2858, sample1.star2())
    }
}