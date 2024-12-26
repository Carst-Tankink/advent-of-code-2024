package day11

import util.Solution

class PlutonianPebbles(fileName: String?) : Solution<List<Long>, Long>(fileName) {
    override fun parse(line: String): List<Long> {
        return line.split(' ').map { it.toLong() }
    }

    override fun solve1(data: List<List<Long>>): Long {
        return data.first().sumOf { blink(it, 25) }
    }

    override fun solve2(data: List<List<Long>>): Long {
        return data.first().sumOf { blink(it, 75) }
    }

    private fun blink(stone: Long, blinks: Int): Long {
        val cache = mutableMapOf<Pair<Long, Int>, Long>()

        fun rec(stone: Long, blinks: Int): Long {
            val cached = cache[stone to blinks]
            return if (cached != null) cached else {
                val result = if (blinks == 0) 1L else {
                    when {
                        stone == 0L -> rec(1L, blinks - 1)
                        stone.toString().length % 2 == 0 -> {
                            val stoneString = stone.toString()
                            val splits = stoneString.chunked(stoneString.length / 2).map { it.toLong() }
                            splits.sumOf { rec(it, blinks - 1) }
                        }

                        else -> rec(stone * 2024, blinks - 1)
                    }


                }

                cache[stone to blinks] = result
                result
            }

        }

        return rec(stone, blinks)
    }
}