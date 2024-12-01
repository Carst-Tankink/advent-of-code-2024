package day1

import util.Solution
import kotlin.math.abs

class HistorianHysteria(fileName: String?) : Solution<Pair<Int, Int>, Long>(fileName) {
    override fun parse(line: String): Pair<Int, Int> {
        val numbers = line.split("\\s+".toRegex()).map { it.toInt() }

        return Pair(numbers[0], numbers[1])
    }


    override fun solve1(data: List<Pair<Int, Int>>): Long {
        val (left, right) = data.unzip()
        return left.sorted()
            .zip(right.sorted()) { x, y -> abs(x - y).toLong() }
            .sum()
    }

    override fun solve2(data: List<Pair<Int, Int>>): Long {
        val (left, right) = data.unzip()

        return left.sumOf { x ->
            x * right.count { it == x }.toLong()
        }
    }
}