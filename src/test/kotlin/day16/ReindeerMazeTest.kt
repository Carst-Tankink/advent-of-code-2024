package day16

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ReindeerMazeTest {
    private val sample1 = ReindeerMaze("/day16/sample1")

    @Test
    fun sample1star1() {
        assertEquals(7036, sample1.star1())
    }

    @Test
    fun sample1star2() {
        assertEquals(45, sample1.star2())
    }

    @Test
    fun sample2star2() {
        val sample2 = ReindeerMaze("/day16/sample2")
        assertEquals(64, sample2.star2())
    }
}