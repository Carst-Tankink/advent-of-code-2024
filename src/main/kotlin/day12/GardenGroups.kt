package day12

import util.Grid
import util.Helpers.Companion.toGrid
import util.Point
import util.Solution

data class Region(val value: Char, val region: Set<Point>)

class GardenGroups(fileName: String?) : Solution<List<Char>, Long>(fileName) {
    override fun parse(line: String): List<Char> = line.toList()

    override fun solve1(data: List<List<Char>>): Long {
        val grid = data.toGrid()

        val regions = calculateRegions(grid.keys, null, emptySet(), grid)

        fun calculateValue(region: Region): Long {
            val points = region.region
            val area = points.size
            val perimeter = points
                .flatMap { it.getNeighbours(true) }
                .count { it !in points }

            return area * perimeter.toLong()
        }

        return regions.sumOf { calculateValue(it) }
    }

    override fun solve2(data: List<List<Char>>): Long {
        TODO("Not yet implemented")
    }

    private tailrec fun calculateRegions(
        pointsLeft: Set<Point>,
        currentRegion: Region?,
        regions: Set<Region>,
        grid: Grid<Char>
    ): Set<Region> {
        return if (pointsLeft.isEmpty() && currentRegion != null) regions + currentRegion else {
            // Try to expand current region
            if (currentRegion == null) {
                val newPoint = pointsLeft.first()
                val value = grid[newPoint]!!
                calculateRegions(pointsLeft - newPoint, Region(value, setOf(newPoint)), regions, grid)
            } else {
                val points = currentRegion.region
                val regionValue = grid[points.first()]
                val neighbours = points.flatMap { it.getNeighbours(true) }
                    .filter { it !in points }
                    .filter { grid[it] == regionValue }
                if (neighbours.isNotEmpty()) {
                    val newRegion = currentRegion.copy(region = currentRegion.region + neighbours)
                    calculateRegions(pointsLeft - neighbours.toSet(), newRegion, regions, grid)
                } else {
                    calculateRegions(pointsLeft, null, regions + currentRegion, grid)
                }
            }
        }
    }
}