package day7

import util.Solution

class BridgeRepair(fileName: String?) : Solution<List<Long>, Long>(fileName) {
    override fun parse(line: String): List<Long> = line.split(":?\\s+".toRegex()).map(String::toLong)

    override fun solve1(data: List<List<Long>>): Long {
        return data
            .filter { possiblyTrue(it.drop(1), it.first(), 0) }
            .sumOf { it.first() }
    }

    private fun possiblyTrue(numbers: List<Long>, calibrationValue: Long, acc: Long): Boolean {
        return if (numbers.isEmpty()) calibrationValue == acc else {
            val next = numbers.first()
            val tail = numbers.drop(1)
            possiblyTrue(tail, calibrationValue, acc + next) || possiblyTrue(tail, calibrationValue, acc * next)
        }
    }

    override fun solve2(data: List<List<Long>>): Long {
        return data
            .filter { possiblyTrueConcat(it.drop(1), it.first(), 0) }
            .sumOf { it.first() }
    }

    private fun possiblyTrueConcat(numbers: List<Long>, calibrationValue: Long, acc: Long): Boolean {
        return if (numbers.isEmpty()) calibrationValue == acc else {
            val next = numbers.first()
            val tail = numbers.drop(1)
            possiblyTrueConcat(tail, calibrationValue, acc + next)
                    || possiblyTrueConcat(tail, calibrationValue, acc * next)
                    || possiblyTrueConcat(tail, calibrationValue, "${acc}${next}".toLong())
        }
    }

}
