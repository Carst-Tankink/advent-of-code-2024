package day2

import util.Solution

class RedNosedReports(fileName: String?) : Solution<List<Int>, Int>(fileName) {
    override fun parse(line: String): List<Int> = line.split(" ").map { it.toInt() }

    override fun solve1(data: List<List<Int>>): Int = data.count {
        isSafe(it)
    }

    private fun isSafe(line: List<Int>): Boolean {
        val differences = line.windowed(2).map { it[0] - it[1] }

        return differences.all { it in 1..3 } or differences.all { -3 <= it && it < 0 }
    }

    override fun solve2(data: List<List<Int>>): Int {
        TODO("Not yet implemented")
    }
}