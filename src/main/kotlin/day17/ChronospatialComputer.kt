package day17

import util.Solution

sealed interface ComputerLine
data class Register(val identifier: String, val content: Long) : ComputerLine
data class Instructions(val ins: List<Int>) : ComputerLine

data class ProgramState(
    val pointer: Int,
    val regA: Long,
    val regB: Long,
    val regC: Long,
    val output: List<Long>
)

class ChronospatialComputer(fileName: String?) : Solution<ComputerLine, String>(fileName) {
    override fun parse(line: String): ComputerLine? {
        val registerRegEx = "Register ([ABC]): (\\d+)".toRegex()
        return when {
            line.isEmpty() -> null
            registerRegEx.matches(line) -> {
                val groups = registerRegEx.find(line)!!.groupValues
                Register(groups[1], groups[2].toLong())
            }

            line.startsWith("Program: ") -> {
                val instructions = line.drop("Program: ".length).split(",").map { it.toInt() }
                Instructions(instructions)
            }

            else -> error("Unmatched input: $line")
        }
    }

    override fun solve1(data: List<ComputerLine>): String {
        val instructions = data.filterIsInstance<Instructions>().first().ins

        val registers = data.filterIsInstance<Register>()
        return interpret(
            instructions,
            ProgramState(
                0,
                registers[0].content,
                registers[1].content,
                registers[2].content,
                emptyList()
            )
        ).joinToString(",")
    }

    private tailrec fun interpret(instructions: List<Int>, programState: ProgramState): List<Long> {
        return if (programState.pointer >= instructions.size) programState.output else {
            val opcode = instructions[programState.pointer]
            val operand = instructions[programState.pointer + 1]

            val newState = doInstruction(opcode, operand, programState)

            interpret(instructions, newState)
        }
    }

    override fun solve2(data: List<ComputerLine>): String {
        val instructions = data.filterIsInstance<Instructions>().first().ins
        val registers = data.filterIsInstance<Register>()
        val computer = ProgramState(
            0,
            registers[0].content,
            registers[1].content,
            registers[2].content,
            emptyList()
        )

        fun findInput(candidate: Long, remainingInstructions: List<Long>): Long? {
            return when {
                interpret(instructions, computer.copy(regA = candidate)) == instructions -> {
                    candidate
                }
                remainingInstructions.isEmpty() -> candidate
                else -> {
                    val shifted = candidate shl 3
                    val others = (shifted..shifted + 7).mapNotNull {
                        val withAttempt = computer.copy(regA = it)
                        val result = interpret(instructions, withAttempt).first()
                        it.takeIf { result == remainingInstructions.last() }
                    }

                    others.minOfOrNull { findInput(it, remainingInstructions.dropLast(1)) ?: Long.MAX_VALUE }
                }
            }
        }


        return findInput(0L, instructions.map { it.toLong() }).toString()
        /*return instructions
            .reversed()
            .map { it.toLong() }
            .fold(listOf(0L)) { candidates, instruction ->
                candidates.flatMap { candidate ->
                    val shifted = candidate shl 3
                    (shifted..shifted + 8).mapNotNull { attempt ->
                        val withAttempt = computer.copy(regA = attempt)
                        val result = interpret(instructions, withAttempt).first()
                        attempt.takeIf { result == instruction }
                    }
                }
            }.toString()*/
    }

    private fun doInstruction(opcode: Int, operand: Int, programState: ProgramState): ProgramState {
        val newPointer = programState.pointer + 2
        return when (opcode) {
            0 -> {
                val numerator = programState.regA
                val denominator = powerTwo(getCombo(operand, programState))

                programState.copy(pointer = newPointer, regA = numerator / denominator)
            }

            1 -> programState.copy(pointer = newPointer, regB = programState.regB xor operand.toLong())
            2 -> programState.copy(pointer = newPointer, regB = getCombo(operand, programState) % 8)
            3 -> if (programState.regA == 0L) programState.copy(pointer = newPointer) else {
                programState.copy(pointer = operand)
            }

            4 -> programState.copy(pointer = newPointer, regB = programState.regB xor programState.regC)
            5 -> programState.copy(
                pointer = newPointer,
                output = programState.output + (getCombo(operand, programState) % 8)
            )

            6 -> {
                val numerator = programState.regA
                val denominator = powerTwo(getCombo(operand, programState))

                programState.copy(pointer = newPointer, regB = numerator / denominator)
            }

            7 -> {
                val numerator = programState.regA
                val denominator = powerTwo(getCombo(operand, programState))

                programState.copy(pointer = newPointer, regC = numerator / denominator)
            }

            else -> error("Unmatched opcode: $opcode")
        }
    }

    private fun getCombo(operand: Int, programState: ProgramState): Long {
        return when (operand) {
            in 0..3 -> operand.toLong()
            4 -> programState.regA
            5 -> programState.regB
            6 -> programState.regC
            7 -> error("Reserved operand")
            else -> error("Not a 3-bit number")
        }
    }

    private tailrec fun powerTwo(n: Long, r: Long = 1): Long {
        return if (n == 0L) r else {
            powerTwo(n - 1, 2 * r)
        }
    }
}