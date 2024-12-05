package day5

import util.Solution

sealed interface InstructionOrLine

data class Instruction(val early: Int, val late: Int) : InstructionOrLine
data class Line(val content: List<Int>) : InstructionOrLine

class PrintQueue(fileName: String?) : Solution<InstructionOrLine, Int>(fileName) {
    override fun parse(line: String): InstructionOrLine? {
        return when {
            line.isEmpty() -> null
            line.contains("|") -> {
                val splits = line.split("|")
                Instruction(splits[0].toInt(), splits[1].toInt())
            }

            else -> Line(line.split(",").map { it.toInt() })
        }
    }

    override fun solve1(data: List<InstructionOrLine>): Int {
        val instructions = data.filterIsInstance<Instruction>()

        val afterInstructions = instructions.groupBy({ it.early }) { it.late }
        val beforeInstructions = instructions.groupBy({ it.late }) {it.early }

        tailrec fun isInOrder(toCheck: List<Int>, before: List<Int>): Boolean {
            return if (toCheck.isEmpty()) true else {
                val head = toCheck.first()
                val after = toCheck.drop(1)

                val needComeAfter = afterInstructions[head] ?: emptyList<Int>()
                val needComeBefore = beforeInstructions[head] ?: emptyList<Int>()

                val afterCorrect = after.none { it in needComeBefore }
                val beforeCorrect = before.none { it in needComeAfter }

                if (afterCorrect && beforeCorrect) isInOrder(after, before + head) else false
            }
        }

        val lines = data.filterIsInstance<Line>().map { it.content }
        val filtered = lines
            .filter { isInOrder(it, emptyList()) }
        return filtered
            .sumOf { it[it.size / 2] }
    }

    override fun solve2(data: List<InstructionOrLine>): Int {
        TODO("Not yet implemented")
    }
}