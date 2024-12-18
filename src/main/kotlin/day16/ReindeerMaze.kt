package day16

import util.Facing
import util.Helpers.Companion.toGrid
import util.Point
import util.Solution

enum class Maze {
    EMPTY,
    WALL,
    START,
    END
}

data class Reindeer(val pos: Point, val dir: Facing)
class ReindeerMaze(fileName: String?) : Solution<List<Maze>, Long>(fileName) {
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

    override fun solve1(data: List<List<Maze>>): Long {
        val maze = data.toGrid()
        val start = maze.filter { it.value == Maze.START }.keys.first()
        val end = maze.filter { it.value == Maze.END }.keys.first()

        val startReindeer = Reindeer(start, Facing.RIGHT)
        val distances: MutableMap<Reindeer, Long> = mutableMapOf(startReindeer to 0)

        fun canTurnLeft(r: Reindeer): Boolean {
            val posToLeft =  when (r.dir) {
                Facing.LEFT -> r.pos + Point(0, 1)
                Facing.RIGHT -> r.pos + Point(0, -1)
                Facing.UP -> r.pos + Point(-1, 0)
                Facing.DOWN -> r.pos + Point(1, 0)
            }

            return maze[posToLeft] != Maze.WALL
        }

        fun canTurnRight(r: Reindeer): Boolean {
            val posToRight =  when (r.dir) {
                Facing.LEFT -> r.pos + Point(0, -1)
                Facing.RIGHT -> r.pos + Point(0, 1)
                Facing.UP -> r.pos + Point(1, 0)
                Facing.DOWN -> r.pos + Point(-1, 0)
            }

            return maze[posToRight] != Maze.WALL
        }

        fun updateShortest(forward: Reindeer, score: Long) {
            val currentShortest = distances[forward] ?: Long.MAX_VALUE
            distances[forward] = minOf(currentShortest, score)
        }

        tailrec fun bestPath(possiblePoints: Set<Reindeer>, visited: Set<Reindeer>): Long {
            return if (distances.keys.any { it.pos == end })
                distances
                .filter { it.key.pos == end }
                .minOf { it.value }
            else {
                val next = possiblePoints.minBy { distances[it] ?: Long.MAX_VALUE }
                val currentDistance = distances[next]!!

                val forward = next.copy(pos = next.pos + next.dir.vector)
                val forwardSet = if (maze[forward.pos] != Maze.WALL) {
                    updateShortest(forward, currentDistance + 1)
                    setOf(forward)
                } else {
                    emptySet()
                }


                val left = if (canTurnLeft(next)) {
                    val element = next.copy(dir = next.dir.turnLeft())
                    updateShortest(element, currentDistance + 1000)
                    setOf(element)
                } else {
                    emptySet()
                }
                val right = if (canTurnRight(next)) {
                    val element = next.copy(dir = next.dir.turnRight())
                    updateShortest(element, currentDistance + 1000)
                    setOf(element)
                } else {
                    emptySet()
                }


                val nextPoints = (possiblePoints - next + forwardSet + left + right)
                    .filter { it !in visited }
                    .toSet()
                bestPath(nextPoints, visited + next)
            }
        }

        return bestPath(setOf(startReindeer), emptySet())
    }

    override fun solve2(data: List<List<Maze>>): Long {
        TODO("Not yet implemented")
    }
}