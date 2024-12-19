package day17

import util.Solution

sealed interface ComputerLine
data class Register(val identifier: String, val content: Int) : ComputerLine
data class Instructions(val ins: List<Int>) : ComputerLine

data class ProgramState(
    val pointer: Int,
    val regA: Int,
    val regB: Int,
    val regC: Int,
    val output: List<Int>
)

class ChronospatialComputer(fileName: String?) : Solution<ComputerLine, String>(fileName) {
    override fun parse(line: String): ComputerLine? {
        val registerRegEx = "Register ([ABC]): (\\d+)".toRegex()
        return when {
            line.isEmpty() -> null
            registerRegEx.matches(line) -> {
                val groups = registerRegEx.find(line)!!.groupValues
                Register(groups[1], groups[2].toInt())
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
            ProgramState(0, registers[0].content, registers[1].content, registers[2].content, emptyList())
        ).joinToString(",")
    }

    private tailrec fun interpret(instructions: List<Int>, programState: ProgramState): List<Int> {
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

        val numbers = generateSequence(0) { it + 1 }

        val quine = numbers.first {
            val output = interpret(instructions, ProgramState(pointer = 0, regA = it, regB = registers[1].content, regC = registers[2].content, output = emptyList()))

            output == instructions
        }

        return quine.toString()
    }

    private fun doInstruction(opcode: Int, operand: Int, programState: ProgramState): ProgramState {
        val newPointer = programState.pointer + 2
        return when (opcode) {
            0 -> {
                val numerator = programState.regA
                val denominator = powerTwo(getCombo(operand, programState))

                programState.copy(pointer = newPointer, regA = numerator / denominator)
            }

            1 -> programState.copy(pointer = newPointer, regB = programState.regB xor operand)
            2 -> programState.copy(pointer = newPointer, regB = getCombo(operand, programState) % 8)
            3 -> if (programState.regA == 0) programState.copy(pointer = newPointer) else {
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

    private fun getCombo(operand: Int, programState: ProgramState): Int {
        return when (operand) {
            in 0..3 -> operand
            4 -> programState.regA
            5 -> programState.regB
            6 -> programState.regC
            7 -> error("Reserved operand")
            else -> error("Not a 3-bit number")
        }
    }

    private tailrec fun powerTwo(n: Int, r: Int = 1): Int {
        return if (n == 0) r else {
            powerTwo(n - 1, 2 * r)
        }
    }
}