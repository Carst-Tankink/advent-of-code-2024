package day8

import util.Helpers.Companion.toGrid
import util.Point
import util.Solution

sealed interface MapPoint
data object EMPTY : MapPoint
data class Antenna(val frequency: Char) : MapPoint

class ResonantCollinearity(fileName: String?) : Solution<List<MapPoint>, Int>(fileName) {
    override fun parse(line: String): List<MapPoint> = line.map {
        if (it == '.') EMPTY else Antenna(it)
    }

    override fun solve1(data: List<List<MapPoint>>): Int {
        val grid = data.toGrid()

        val antennae = grid.filter { it.value is Antenna }.mapValues { it.value as Antenna }

        val antinodes: Set<Point> = antennae
            .flatMap {
                val currPoint = it.key
                val otherAntennae = antennae.filter { a -> a.value.frequency == it.value.frequency }
                    .keys - currPoint

                otherAntennae.map { (x, y) ->
                    val diffX = currPoint.x - x
                    val diffY = currPoint.y - y

                    currPoint + Point(diffX, diffY)
                }.filter { p -> p in grid }.toSet()

            }
            .toSet()

        return antinodes.size
    }

    override fun solve2(data: List<List<MapPoint>>): Int {
        val grid = data.toGrid()

        val antennae = grid.filter { it.value is Antenna }.mapValues { it.value as Antenna }

        val antinodes: Set<Point> = antennae
            .flatMap { p ->
                val currPoint = p.key
                val otherAntennae = antennae.filter { a -> a.value.frequency == p.value.frequency }
                    .keys - currPoint


                val diffs = otherAntennae.map { (x, y) ->
                    val diffX = currPoint.x - x
                    val diffY = currPoint.y - y

                    Point(diffX, diffY)
                }

                diffs.flatMap { d ->
                    generateSequence(currPoint) { it - d }.takeWhile { it in grid }.toSet()
                    generateSequence(currPoint) { it + d }.takeWhile { it in grid }.toSet()
                }.toSet()
            }
            .toSet()

        return antinodes.size
    }
}