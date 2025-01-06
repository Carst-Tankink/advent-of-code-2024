package day21

import util.Point
import util.Solution

class KeypadConundrum(fileName: String?) : Solution<String, Long>(fileName) {
    override fun parse(line: String): String = line

    //@formatter:off
    private val numericKeys = mapOf(
        Point(0, 0) to '7', Point(1, 0) to '8', Point(2, 0) to '9',
        Point(0, 1) to '4', Point(1, 1) to '5', Point(2, 1) to '6',
        Point(0, 2) to '1', Point(1, 2) to '2', Point(2, 2) to '3',
                                   Point(1, 3) to '0', Point(2, 3) to 'A'
    )

    private val cursorKeys = mapOf(
                                   Point(1, 0) to '^', Point(2, 0) to 'A',
        Point(0, 1) to '<', Point(1, 1) to 'v', Point(2, 1) to '>'
    )
    //@formatter:on

    private val numericPaths: Map<Pair<Char, Char>, Set<String>> = getAllShortestPaths(numericKeys)
    private val cursorPaths: Map<Pair<Char, Char>, Set<String>> = getAllShortestPaths(cursorKeys)

    private fun getAllShortestPaths(keyMap: Map<Point, Char>): Map<Pair<Char, Char>, Set<String>> {
        val keys = keyMap.keys
        val allPairs = keys.flatMap { v1 -> keys.map { v2 -> v1 to v2 } }

        fun findAllShortestPaths(start: Point, end: Point): Set<String> {
            tailrec fun dijkstra(
                queue: Set<List<Point>>,
                distances: Map<Point, Int>,
                shortestPaths: Set<String> = emptySet()
            ): Set<String> {
                return if (queue.isEmpty()) shortestPaths
                else {
                    val path = queue.minBy { distances[it.last()] ?: Int.MAX_VALUE }
                    val tail = queue - setOf(path)
                    if (path.last() == end && (shortestPaths.isEmpty() || path.size + 1 == shortestPaths.first().length + 1)) {
                        val directionMap = path.zipWithNext { from, to ->
                            when (val diff = to - from) {
                                Point(1, 0) -> '>'
                                Point(-1, 0) -> '<'
                                Point(0, 1) -> 'v'
                                Point(0, -1) -> '^'
                                else -> error("Unexpected diff: $diff")
                            }
                        }.joinToString("") + "A"
                        dijkstra(tail, distances, shortestPaths + setOf(directionMap))
                    } else {
                        val nextDistance = distances.getValue(path.last()) + 1
                        val nextPoints = path
                            .last()
                            .getNeighbours(true)
                            .filter { it in keyMap }
                            .filter { it !in path }
                            .filter {
                                val distToPoint = distances[it] ?: Int.MAX_VALUE
                                nextDistance <= distToPoint
                            }

                        val nextDistances = nextPoints.associateWith { nextDistance }
                        val nextPaths = nextPoints
                            .map { path + it }
                            .toSet()
                        dijkstra(
                            tail + nextPaths,
                            distances + nextDistances,
                            shortestPaths
                        )
                    }
                }
            }
            return dijkstra(setOf(listOf(start)), mapOf(start to 0))
        }
        return allPairs.associateBy({ keyMap.getValue(it.first) to keyMap.getValue(it.second) }) {
            findAllShortestPaths(it.first, it.second)
        }

    }

    override fun solve1(data: List<String>): Long {
        return solveForDepth(data, 2)
    }


    override fun solve2(data: List<String>): Long {
        return solveForDepth(data, 25)
    }

    private fun solveForDepth(data: List<String>, depth: Int): Long {
        return data.sumOf {
            val pathLength = findLength(it, depth)
            pathLength * it.dropLast(1).toLong()
        }
    }

    private fun findLength(
        code: String,
        depth: Int,
        transitions: Map<Pair<Char, Char>, Set<String>> = numericPaths,
        cache: MutableMap<Pair<String, Int>, Long> = mutableMapOf()
    ): Long {
        return cache.getOrPut(code to depth) {
            code.fold('A' to 0L) { (prev, cost), c ->
                val paths = transitions.getValue(prev to c)
                val pathCost = if (depth == 0) {
                    paths.minOf { it.length }.toLong()
                } else {
                    paths.minOf { p -> findLength(p, depth - 1, cursorPaths, cache) }
                }
                (c to (cost + pathCost))
            }.second
        }
    }
}