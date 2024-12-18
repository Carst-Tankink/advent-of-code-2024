package day16

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ReindeerMazeTest {
    private val sample1 = ReindeerMaze("/day16/sample1")

    @Test
    fun sample1star1() {
        assertEquals(7036, sample1.star1())
    }
}