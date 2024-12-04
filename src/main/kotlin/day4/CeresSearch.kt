package day4

import util.Grid
import util.Helpers.Companion.toGrid
import util.Point
import util.Solution

class CeresSearch(fileName: String?) : Solution<List<Char>, Int>(fileName) {
    override fun parse(line: String): List<Char> = line.toList()

    override fun solve1(data: List<List<Char>>): Int {
        val grid = data.toGrid()


        fun findXmas(p: Point): Int {
            val offset = 0..3L
            val negativeX = offset.map { Point(-it, 0) }
            val positiveX = offset.map { Point(it, 0) }
            val negativeY = offset.map { Point(0, -it) }
            val positiveY = offset.map { Point(0, it) }
            val diagRD = offset.flatMap { x -> offset.map { y -> Point(x, y) } }.filter { it.x == it.y } // RightDown
            val diagLD = offset.flatMap { x -> offset.map { y -> Point(-x, y) } }.filter { it.x == -it.y }
            val diagLU = offset.flatMap { x -> offset.map { y -> Point(-x, -y) } }.filter { it.x == it.y }
            val diagRU = offset.flatMap { x -> offset.map { y -> Point(x, -y) } }.filter { it.x == -it.y }

            val words = setOf(
                negativeX, positiveX,
                negativeY, positiveY,
                diagRD, diagLU, diagRU, diagLD
            ).map { line ->
                line.map { p + it }
            }.map { it.extractWord(grid) }
            return words.count { it == "XMAS" }
        }

        return grid.keys.sumOf {
            findXmas(it)
        }
    }

    override fun solve2(data: List<List<Char>>): Int {
        val grid = data.toGrid()

        fun isMas(s: String): Boolean {
            return s == "MAS" || s == "SAM"
        }

        fun findXMas(p: Point): Boolean {
            val topLeftBottomRight = listOf(
                Point(p.x - 1, p.y - 1),
                p,
                Point(p.x + 1, p.y + 1)
            ).extractWord(grid)

            val topRightBottomLeft = listOf(
                Point(p.x + 1, p.y - 1),
                p,
                Point(p.x - 1, p.y + 1),
            ).extractWord(grid)

            return isMas(topLeftBottomRight) && isMas(topRightBottomLeft)
        }
        return grid.keys.count { findXMas(it) }
    }

    private fun List<Point>.extractWord(grid: Grid<Char>) =
        this.map { grid[it] ?: '_' }.joinToString("")
}