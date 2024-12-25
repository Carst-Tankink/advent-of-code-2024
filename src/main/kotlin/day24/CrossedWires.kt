package day24

import util.Helpers
import util.Solution

enum class Operator {
    AND,
    OR,
    XOR;

    companion object {
        fun fromString(s: String): Operator {
            return when (s) {
                "AND" -> AND
                "OR" -> OR
                "XOR" -> XOR
                else -> error("Unknown operator $s")
            }
        }

    }
}

sealed interface Wires
data class Input(val gate: String, val value: Int) : Wires
data class Gate(val left: String, val right: String, val operator: Operator, val result: String) : Wires

class CrossedWires(fileName: String?) : Solution<Wires, String>(fileName) {
    override fun parse(line: String): Wires? {
        return when {
            line.contains(":") -> {
                val split = line.split(": ")
                Input(split[0], split[1].toInt())
            }

            line.isEmpty() -> null
            else -> {
                val (operation, target) = line.split(" -> ")

                val (left, operator, right) = operation.split(" ")
                Gate(
                    left = left,
                    right = right,
                    Operator.fromString(operator),
                    result = target
                )
            }
        }
    }

    override fun solve1(data: List<Wires>): String {
        return computeResult(data).toString()
    }

    private fun computeResult(data: List<Wires>): Long {
        val operatorMap = data.associateBy {
            when (it) {
                is Input -> it.gate
                is Gate -> it.result
            }
        }

        fun evaluate(wire: Wires): Int {
            return when (wire) {
                is Input -> wire.value
                is Gate -> {
                    val left = evaluate(operatorMap[wire.left]!!)
                    val right = evaluate(operatorMap[wire.right]!!)

                    when (wire.operator) {
                        Operator.XOR -> if (left != right) 1 else 0
                        Operator.AND -> if (left == 1 && right == 1) 1 else 0
                        Operator.OR -> if (left == 1 || right == 1) 1 else 0
                    }

                }
            }
        }

        val outputs = data
            .filterIsInstance<Gate>()
            .filter { it.result.startsWith("z") }
            .sortedByDescending { it.result.drop(1).toInt() }
            .map { evaluate(it) }

        return Helpers.toDecimal(outputs, 2)
    }

    override fun solve2(data: List<Wires>): String {
        val patches = listOf(
            "z07" to "gmt",
            "qjj" to "cbj",
            "z18" to "dmn",
            "z35" to "cfk"
        )
        val patched = patch(
            data, patches
        )
        val operatorMap = patched.associateBy {
            when (it) {
                is Input -> it.gate
                is Gate -> it.result
            }
        }

        val xBits = operatorMap
            .filter { it.key.startsWith("x") }
            .entries
            .sortedByDescending { it.key.drop(1).toInt() }
            .map { it.value }
            .filterIsInstance<Input>()
            .map { it.value }

        val xInput = Helpers.toDecimal(xBits, 2)

        val yBits = operatorMap
            .filter { it.key.startsWith("y") }
            .entries
            .sortedByDescending { it.key.drop(1).toInt() }
            .map { it.value }
            .filterIsInstance<Input>()
            .map { it.value }

        val yIn = Helpers.toDecimal(yBits, 2)


        val expectedResult = xInput + yIn
        val actualResult = computeResult(patched)

        val expectedBinary = toBinary(expectedResult, emptyList())
        val actualBinary = toBinary(actualResult, emptyList())

        val mismatches = expectedBinary.zip(actualBinary)
            .withIndex()
            .filter { it.value.first != it.value.second }

        if (mismatches.isEmpty()) {
            return patches.flatMap { listOf(it.first, it.second) }
                .sorted()
                .joinToString(",")
        } else {
            return "Mismatches: $mismatches"
        }
    }

    private fun patch(data: List<Wires>, patches: List<Pair<String, String>>): List<Wires> {
        return data.fold(emptyList()) { l, w ->
            when (w) {
                is Gate -> {
                    val patch = patches.find { it.first == w.result}?.second ?:
                        patches.find { it.second == w.result }?.first
                    if (patch == null) l + w else
                        l + w.copy(result = patch)
                }
                is Input -> l + w
            }
        }
    }


    private tailrec fun toBinary(number: Long, binary: List<Int>): List<Int> {
        return if (number == 0L) binary else {
            val remainder = (number % 2).toInt()

            toBinary(number / 2, binary + listOf(remainder))
        }
    }
}