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
        val beforeInstructions = instructions.groupBy({ it.late }) { it.early }

        return data
            .filterIsInstance<Line>()
            .map { it.content }
            .filter { isInOrder(it, emptyList(), afterInstructions, beforeInstructions) }
            .sumOf { it[it.size / 2] }
    }

    override fun solve2(data: List<InstructionOrLine>): Int {
        val instructions = data.filterIsInstance<Instruction>()

        val afterInstructions = instructions.groupBy({ it.early }) { it.late }
        val beforeInstructions = instructions.groupBy({ it.late }) { it.early }

        val sorted = data
            .filterIsInstance<Line>()
            .map { it.content }
            .filterNot { isInOrder(it, emptyList(), afterInstructions, beforeInstructions) }
            .map { sortBy(it, emptyList(), afterInstructions, beforeInstructions) }
        return sorted
            .sumOf { it[it.size / 2] }
    }

    private tailrec fun sortBy(
        toSort: List<Int>,
        sorted: List<Int>,
        afterIns: Map<Int, List<Int>>,
        beforeIns: Map<Int, List<Int>>
    ): List<Int> {
        return if (toSort.isEmpty()) sorted else {
            val head = toSort.first()
            val newSorted = insertInPosition(head, emptyList(), sorted, afterIns, beforeIns)
            sortBy(toSort.drop(1), newSorted, afterIns, beforeIns)
        }
    }

    private tailrec fun insertInPosition(
        head: Int,
        preceding: List<Int>,
        following: List<Int>,
        afterIns: Map<Int, List<Int>>,
        beforeIns: Map<Int, List<Int>>
    ): List<Int> {
        val currentList = preceding + head + following
        return if (isInOrder(currentList, emptyList(), afterIns, beforeIns)) currentList else {
            val next = following.first()
            val newPreceding = if (next in beforeIns[head]!!) preceding + next else preceding
            insertInPosition(head, newPreceding, following.drop(1), afterIns, beforeIns)
        }
    }

    private tailrec fun isInOrder(
        toCheck: List<Int>,
        before: List<Int>,
        afterIns: Map<Int, List<Int>>,
        beforeIns: Map<Int, List<Int>>
    ): Boolean {
        return if (toCheck.isEmpty()) true else {
            val head = toCheck.first()
            val after = toCheck.drop(1)

            val needComeAfter = afterIns[head] ?: emptyList()
            val needComeBefore = beforeIns[head] ?: emptyList()

            val afterCorrect = after.none { it in needComeBefore }
            val beforeCorrect = before.none { it in needComeAfter }

            if (afterCorrect && beforeCorrect) isInOrder(
                after,
                before + head,
                afterIns,
                beforeIns,
            ) else false
        }
    }
}