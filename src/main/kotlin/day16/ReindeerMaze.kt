package day16

import util.Facing
import util.Grid
import util.Helpers.Companion.toGrid
import util.Point
import util.Solution
import java.util.PriorityQueue

enum class Maze {
    EMPTY,
    WALL,
    START,
    END
}

data class Reindeer(val pos: Point, val dir: Facing)
class ReindeerMaze(fileName: String?) : Solution<List<Maze>, Int>(fileName) {
    override fun parse(line: String): List<Maze> {
        return line.map {
            when (it) {
                '.' -> Maze.EMPTY
                '#' -> Maze.WALL
                'S' -> Maze.START
                'E' -> Maze.END
                else -> error("Unexpected maze element: $it")
            }
        }
    }

    override fun solve1(data: List<List<Maze>>): Int {
        val maze = data.toGrid()
        val start = maze.filter { it.value == Maze.START }.keys.first()
        val startReindeer = Reindeer(start, Facing.RIGHT)

        return bestPath(setOf(startReindeer), maze)
    }

    override fun solve2(data: List<List<Maze>>): Int {
        val maze = data.toGrid()
        val start = maze.filter { it.value == Maze.START }.keys.first()
        val end = maze.filter { it.value == Maze.END }.keys.first()
        val startReindeer = Reindeer(start, Facing.RIGHT)

        val cheapestPath = bestPath(setOf(startReindeer), maze)
        val startPath = listOf(startReindeer)

        val queue = PriorityQueue<Pair<List<Reindeer>, Int>>(compareBy { it.second })
            .apply { add(startPath to 0) }

        val costs: MutableMap<Reindeer, Int> = mutableMapOf()

        tailrec fun findAllShortestPaths(
            pointsInShortest: Set<Point>,
        ): Set<Point> {
            return if (queue.isEmpty()) pointsInShortest else {
                val (path, cost) = queue.poll()
                when {
                    (cost > cheapestPath) -> pointsInShortest
                    (path.first().pos == end && cost == cheapestPath) ->
                        findAllShortestPaths(pointsInShortest + path.map { it.pos }.toSet())

                    (costs.getOrDefault(path.first(), Int.MAX_VALUE) >= cost) -> {
                        costs[path.first()] = cost

                        getNextMoves(path.first(), cost)
                            .filter { maze[it.first.pos] != Maze.WALL }
                            .forEach {
                                val l = listOf(it.first) + path
                                queue.add(l to it.second)
                            }

                        findAllShortestPaths(pointsInShortest)
                    }

                    else -> findAllShortestPaths(pointsInShortest)
                }

            }


        }

        return findAllShortestPaths(emptySet()).size
    }

    private fun getNextMoves(r: Reindeer, currentDistance: Int): Set<Pair<Reindeer, Int>> {
        val forward = r.copy(pos = r.pos + r.dir.vector)
        val left = r.copy(dir = r.dir.turnLeft())
        val right = r.copy(dir = r.dir.turnRight())
        return setOf(
            forward to currentDistance + 1,
            left to currentDistance + 1000,
            right to currentDistance + 1000
        )
    }

    private fun bestPath(possiblePoints: Set<Reindeer>, maze: Grid<Maze>): Int {
        val starReindeer = possiblePoints.first()
        val end = maze.filter { it.value == Maze.END }.keys.first()

        val queue = PriorityQueue<Pair<Reindeer, Int>>(compareBy { it.second })
            .apply { add(Pair(starReindeer, 0)) }
        val distances: MutableMap<Reindeer, Int> = mutableMapOf()

        tailrec fun rec(): Int {
            return if (queue.isEmpty()) Int.MAX_VALUE else {
                val (next, cost) = queue.poll()
                if (next.pos == end) cost else if (distances.getOrDefault(next, Int.MAX_VALUE) > cost) {
                    distances[next] = cost
                    getNextMoves(next, cost)
                        .filter { maze[it.first.pos] != Maze.WALL }
                        .forEach { queue.add(it) }

                    rec()
                } else {
                    rec()
                }
            }
        }

        return rec()
    }
}