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
    ROBOT;

    override fun toString(): String {
        return when (this) {
            EMPTY -> "."
            WALL -> "#"
            BOX -> "O"
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

        val moves = data.filterIsInstance<Moves>().flatMap { it.move }

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
        moves.fold(robot) { pos, direction ->
            if (tryMove(pos, direction)) {
                pos + direction.vector
            } else {
                pos
            }
        }
        return grid.filterValues { it == Warehouse.BOX }
            .entries
            .sumOf { (pos, _) ->
                pos.x + 100 * pos.y
            }
    }

    override fun solve2(data: List<WarehouseOrMoves>): Long {

        TODO("Not yet implemented")
    }
}