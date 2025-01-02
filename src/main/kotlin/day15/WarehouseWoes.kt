package day15

import util.Facing
import util.Helpers.Companion.printGrid
import util.Helpers.Companion.toGrid
import util.Point
import util.Solution

sealed interface WarehouseOrMoves
enum class Warehouse {
    WALL,
    EMPTY,
    BOX,
    LEFT_BOX,
    RIGHT_BOX,
    ROBOT;

    override fun toString(): String {
        return when (this) {
            EMPTY -> "."
            WALL -> "#"
            BOX -> "O"
            LEFT_BOX -> "["
            RIGHT_BOX -> "]"
            ROBOT -> "@"
        }
    }
}

data class WarehouseParts(val warehouse: List<Warehouse>) : WarehouseOrMoves
data class Moves(val move: List<Facing>) : WarehouseOrMoves

class WarehouseWoes(fileName: String?) : Solution<WarehouseOrMoves, Long>(fileName) {
    override fun parse(line: String): WarehouseOrMoves? {
        return when {
            line.startsWith("#") -> {
                WarehouseParts(line.map {
                    when (it) {
                        '#' -> Warehouse.WALL
                        '.' -> Warehouse.EMPTY
                        'O' -> Warehouse.BOX
                        '@' -> Warehouse.ROBOT
                        '[' -> Warehouse.LEFT_BOX
                        ']' -> Warehouse.RIGHT_BOX
                        else -> error("Unexpected character $it")
                    }
                })
            }

            line.isEmpty() -> null
            else -> {
                return Moves(line.map {
                    when (it) {
                        '<' -> Facing.LEFT
                        '>' -> Facing.RIGHT
                        '^' -> Facing.UP
                        'v' -> Facing.DOWN
                        else -> error("Unexpected character $it")
                    }
                })
            }
        }
    }

    override fun solve1(data: List<WarehouseOrMoves>): Long {
        val grid = data.filterIsInstance<WarehouseParts>().map { it.warehouse }.toGrid().toMutableMap()
        fun tryMove(pos: Point, direction: Facing): Boolean {
            val atCurrent = grid[pos]!!
            val nextPos = pos + direction.vector
            return when (grid[nextPos]) {
                Warehouse.EMPTY -> {
                    grid[pos] = Warehouse.EMPTY
                    grid[nextPos] = atCurrent
                    true
                }

                Warehouse.BOX -> {
                    if (tryMove(nextPos, direction)) {
                        grid[pos] = Warehouse.EMPTY
                        grid[nextPos] = atCurrent
                        true
                    } else {
                        false
                    }
                }

                Warehouse.ROBOT -> error("Not possible to move to same robot")
                else -> false
            }

        }

        val robot = grid.entries.first { it.value == Warehouse.ROBOT }.key
        data.filterIsInstance<Moves>()
            .flatMap { it.move }
            .fold(robot) { pos, direction -> if (tryMove(pos, direction)) pos + direction.vector else pos }

        return grid.filterValues { it == Warehouse.BOX }
            .entries
            .sumOf { (pos, _) ->
                pos.x + 100 * pos.y
            }
    }

    override fun solve2(data: List<WarehouseOrMoves>): Long {
        val originalGrid = data.filterIsInstance<WarehouseParts>().map { it.warehouse }.toGrid()
        val grid = printGrid(originalGrid).split("\n").map {
            it.replace("#", "##")
                .replace("O", "[]")
                .replace(".", "..")
                .replace("@", "@.")
        }
            .map { parse(it) }
            .filterIsInstance<WarehouseParts>()
            .map { it.warehouse }
            .toGrid()
            .toMutableMap()


        fun findMoves(
            toVisit: Set<Point>,
            direction: Facing,
            canPush: Set<Point>
        ): Set<Point>? {
            return if (toVisit.isEmpty()) canPush else {
                val point = toVisit.first()
                val tail = toVisit.drop(1).toSet()

                val nextPoint = point + direction.vector

                // TODO: Need to check how to properly deal with the vertical moves of the box. Probably easiest to push them in toVisit in the future.
                // Same goes for the other side of a box in horizontal moves. Can we just add box sides filtered by what we have processed already?
                return when (grid[nextPoint]) {
                    Warehouse.EMPTY -> findMoves(tail, direction, setOf(point) + canPush)
                    Warehouse.LEFT_BOX -> {
                        val rightSide = nextPoint + Facing.RIGHT.vector
                        if (rightSide !in canPush && (direction == Facing.RIGHT || !direction.isHorizontal())) {
                            findMoves(tail + setOf(nextPoint, rightSide), direction, setOf(point) + canPush)
                        } else {
                            findMoves(tail + setOf(nextPoint), direction, setOf(point) + canPush)
                        }
                    }

                    Warehouse.RIGHT_BOX -> {
                        val leftSide = nextPoint + Facing.LEFT.vector
                        if (leftSide !in canPush) {
                            findMoves(tail + setOf(nextPoint, leftSide), direction, setOf(point) + canPush)
                        } else {
                            findMoves(tail + setOf(nextPoint), direction, setOf(point) + canPush)
                        }
                    }

                    Warehouse.ROBOT -> error("Unexpected robot")
                    Warehouse.BOX -> error("Unexpected box")
                    else -> null
                }
            }
        }

        val robot = grid.entries.first { it.value == Warehouse.ROBOT }.key
        data.filterIsInstance<Moves>()
            .flatMap { it.move }
            .fold(robot) { pos, direction ->
                val moves = findMoves(setOf(pos), direction, emptySet())
                    moves?.forEach { from ->
                        // TODO: Fix how to properly move the points
                        val to = from + direction.vector
                        val atFrom = grid[from]!!
                        val atTo = grid[to]!!
                        grid[to] = atFrom
                        grid[from] = atTo
                    }

                if (moves == null) pos else pos + direction.vector

            }

        return grid.filterValues { it == Warehouse.LEFT_BOX }
            .entries
            .sumOf { (pos, _) ->
                pos.x + 100 * pos.y
            }
    }
}