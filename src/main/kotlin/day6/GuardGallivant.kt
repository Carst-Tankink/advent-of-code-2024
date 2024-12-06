package day6

import util.Facing
import util.Helpers.Companion.toGrid
import util.Point
import util.Solution

enum class MapPosition {
    START, FREE, BLOCKED
}

data class Guard(val pos: Point, val facing: Facing)

class GuardGallivant(fileName: String?) : Solution<List<MapPosition>, Int>(fileName) {
    override fun parse(line: String): List<MapPosition> =
        line.map {
            when (it) {
                '.' -> MapPosition.FREE
                '#' -> MapPosition.BLOCKED
                '^' -> MapPosition.START
                else -> error("Unexpected '$it'")
            }
        }


    override fun solve1(data: List<List<MapPosition>>): Int {
        val map = data.toGrid()
        val start = map.entries.find { it.value == MapPosition.START }!!.key
        return walkGuard(Guard(start, Facing.UP), emptySet()) {map[it]}
            ?.map { it.pos }
            ?.toSet()
            ?.size ?: 0
    }



    override fun solve2(data: List<List<MapPosition>>): Int {
        val map = data.toGrid()
        val start = map.entries.find { it.value == MapPosition.START }!!.key
        val startGuard = Guard(start, Facing.UP)
        val possiblePositions = walkGuard(startGuard, emptySet()) { map[it] }
            ?.map { it.pos }
            ?.filter { it != start }
            ?.toSet()!!

        fun isLoop(p: Point): Boolean {
            return walkGuard(startGuard, emptySet(), { if (it == p) MapPosition.BLOCKED else map[it] }) == null
        }

        tailrec fun checkLoops(pts: Set<Point>, wasLoop: Int) : Int {
            return if (pts.isEmpty()) wasLoop else {
                val p = pts.first()
                val t = pts - p
                val looped = isLoop(p)

                if (looped) checkLoops(t, wasLoop + 1) else checkLoops(t, wasLoop)
            }
        }

        return checkLoops(possiblePositions, 0)
    }

    private tailrec fun walkGuard(guard: Guard, visited: Set<Guard>, lookup: (Point) -> MapPosition?): Set<Guard>? {
        return if (guard in visited) null else {
            val nextPos = guard.pos + guard.facing.vector
            val status = lookup(nextPos)
            when (status) {
                null -> return visited + guard // Out of map
                MapPosition.FREE -> walkGuard(guard.copy(pos = nextPos), visited + guard, lookup)
                MapPosition.START -> walkGuard(guard.copy(pos = nextPos), visited + guard, lookup)
                MapPosition.BLOCKED -> walkGuard(guard.copy(facing = guard.facing.turnRight()), visited + guard, lookup)
            }
        }

    }
}