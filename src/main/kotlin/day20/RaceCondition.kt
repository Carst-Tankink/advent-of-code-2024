package day20

import util.Grid
import util.Helpers.Companion.toGrid
import util.Point
import util.Solution

class RaceCondition(fileName: String?) : Solution<List<Char>, Int>(fileName) {
    override fun parse(line: String): List<Char> {
        return line.toList()
    }

    private var track: List<Point> = emptyList()
    override fun solve1(data: List<List<Char>>): Int {
        track = findTrack(data.toGrid())
        return findCheats(track, 100, 2)
    }

    override fun solve2(data: List<List<Char>>): Int {
        return findCheats(track, 100, 20)

    }

    private fun findCheats(track: List<Point>, savingsGoal: Int, cheatTime: Int): Int {
        return track.indices.sumOf { cheatStart ->
            (cheatStart + savingsGoal .. track.lastIndex).count { cheatEnd ->
                val distance = track[cheatStart].manhattanDistance(track[cheatEnd])
                distance <= cheatTime // Can get from cheatStart to cheatEnd in the time given
                        && distance <= cheatEnd - cheatStart - savingsGoal // Saves enough: distance travelled is less than track  distance gained, and within bounds
            }
        }
    }

    private fun findTrack(map: Grid<Char>): List<Point> {

        tailrec fun rec(path: List<Point>): List<Point> {
            val currentPos = path.last()
            return if (map[currentPos] == 'E') path else {
                val next = currentPos.getNeighbours(true)
                    .filter { it !in path && map[it] != '#' }
                rec(path + next)
            }
        }

        return rec(listOf(map.entries.first { it.value == 'S' }.key))
    }
}