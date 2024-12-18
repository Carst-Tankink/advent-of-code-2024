package day18

import util.Point
import util.Solution

class RAMRun(fileName: String?) : Solution<Point, Int>(fileName) {
    override fun parse(line: String): Point {
        val coordinates = line.split(',').map { it.toLong() }
        return Point(coordinates[0], coordinates[1])
    }

    override fun solve1(data: List<Point>): Int {
        return findPath(Point(70, 70), data.take(1024).toSet())!!
    }

    fun findPath(end: Point, corrupt: Set<Point>): Int? {
        val distances: MutableMap<Point, Int> = mutableMapOf()

        distances[Point(0, 0)] = 0
        tailrec fun shortestPath(toVisit: Set<Point>, visited: Set<Point>): Int? {
            return if (end in distances) distances[end]!!
            else if (toVisit.isEmpty()) null else {
                val current = toVisit.minBy { distances[it]!! }
                val dist = distances[current] ?: Int.MAX_VALUE

                val next = current.getNeighbours(true)
                    .filter { !visited.contains(it) }
                    .filter { !corrupt.contains(it) }
                    .filter { it.x >= 0 && it.y >= 0 && it.x <= end.x && it.y <= end.y }

                next.forEach {
                    val curr = distances[it] ?: Int.MAX_VALUE
                    distances[it] = minOf(dist + 1, curr)
                }

                shortestPath(toVisit - current + next, visited + current)
            }
        }

        return shortestPath(setOf(Point(0, 0)), emptySet())
    }

    override fun solve2(data: List<Point>): Int {
        val p = findBlocking(Point(70, 70), data, 1024)

        println("First blocking $p")

        return -1
    }

    fun findBlocking(end: Point, corrupt: List<Point>, start: Int): Point {
        val firstBlocking = (start..corrupt.size)
            .first {
                if (it % 100 == 0) {
                    println("At $it")
                }
                val corrupted = corrupt.take(it).toSet()
                findPath(end, corrupted) == null
            }

        return corrupt[firstBlocking - 1]
    }
}