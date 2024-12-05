package day5

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PrintQueueTest {

    private val sample1 = PrintQueue("/day5/sample1")

    @Test
    fun sample1star1() {
        assertEquals(143, sample1.star1())
    }

}