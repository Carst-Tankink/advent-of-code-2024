package day12

import util.Facing
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
        val grid = data.toGrid()
        val regions = calculateRegions(grid.keys, null, emptySet(), grid)

        fun countCorners(point: Point): Int {
            return listOf(Facing.UP, Facing.RIGHT, Facing.DOWN, Facing.LEFT, Facing.UP)
                .zipWithNext()
                .map { (first, second) ->
                    listOf(
                        grid[point],
                        grid[point + first.vector],
                        grid[point + second.vector],
                        grid[point + first.vector + second.vector]
                    )
                }
                .count { (target, side1, side2, corner) ->
                    (target != side1 && target != side2) || // Outer corner
                            (target == side1 && target == side2 && target != corner) // Inner corner
                }
        }

        fun calculateValueBulk(region: Region): Long {
            val points = region.region
            val area = points.size

            val sides = points.sumOf { countCorners(it) }

            return area * sides.toLong()
        }

        return regions.sumOf { calculateValueBulk(it) }
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