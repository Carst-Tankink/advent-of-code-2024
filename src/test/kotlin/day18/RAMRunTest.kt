package day18

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import util.Point

class RAMRunTest {
    @Test
    fun sample1Star1() {
        val corrupt = setOf(
            Point(5, 4),
            Point(4, 2),
            Point(4, 5),
            Point(3, 0),
            Point(2, 1),
            Point(6, 3),
            Point(2, 4),
            Point(1, 5),
            Point(0, 6),
            Point(3, 3),
            Point(2, 6),
            Point(5, 1),
            Point(1, 2),
            Point(5, 5),
            Point(2, 5),
            Point(6, 5),
            Point(1, 4),
            Point(0, 4),
            Point(6, 4),
            Point(1, 1),
            Point(6, 1),
            Point(1, 0),
            Point(0, 5),
            Point(1, 6),
            Point(2, 0)
        )
        val sample1 = RAMRun("/day18/sample1")
        assertEquals(22, sample1.findPath(Point(6,6), corrupt.take(12).toSet()))
    }
}