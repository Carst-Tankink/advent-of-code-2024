package day10

import util.Helpers.Companion.toGrid
import util.Point
import util.Solution

class HoofIt(fileName: String?) : Solution<List<Int>, Long>(fileName) {
    override fun parse(line: String): List<Int> = line.map { it.digitToInt() }

    override fun solve1(data: List<List<Int>>): Long {
        val heights = data.toGrid()
        tailrec fun reachable(toVisit: Set<Point>, visited: Set<Point>): Long {
            return if (toVisit.isEmpty()) visited.count { heights[it] == 9 }.toLong() else {
                val next = toVisit.first()
                val height = heights[next]!!
                val tail = toVisit.drop(1)
                val neighbours = next
                    .getNeighbours(true)
                    .filterNot { it in visited }
                    .filter { heights[it] == height + 1 }
                    .toSet()
                reachable(neighbours + tail, visited + setOf(next))
            }
        }

        val trailHeads = heights.filterValues { it == 0 }.keys
        return trailHeads.sumOf { reachable(setOf(it), emptySet()) }
    }

    override fun solve2(data: List<List<Int>>): Long {
        val heights = data.toGrid()
        tailrec fun rating(trails: Set<List<Point>>, rating: Long): Long {
            return if (trails.isEmpty()) rating else {
                val nextTrail = trails.first()
                val tail = trails.drop(1).toSet()
                return if (nextTrail.size == 10) rating(tail, rating + 1) else {
                    val point = nextTrail.first()
                    val height = heights[point]!!
                    val neighbours = point
                        .getNeighbours(true)
                        .filter { heights[it] == height + 1 }

                    val newTrails = neighbours.map {  listOf(it) + nextTrail }.toSet()

                    rating(newTrails + tail, rating)
                }
            }
        }

        val trailHeads = heights.filterValues { it == 0 }.keys
        return trailHeads.sumOf { rating(setOf(listOf(it)), 0) }
    }
}