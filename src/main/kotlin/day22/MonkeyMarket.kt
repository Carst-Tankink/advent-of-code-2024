package day22

import util.Solution

class MonkeyMarket(fileName: String?) : Solution<Long, Long>(fileName) {
    override fun parse(line: String): Long = line.toLong()

    override fun solve1(data: List<Long>): Long {
        return data.sumOf { generateSecrets(it).drop(2000).first() }

    }

    private fun Long.mix(other: Long): Long = this xor other
    private fun Long.prune(): Long = this % 16777216
    private fun generateSecrets(seed: Long): Sequence<Long> = generateSequence(seed) { secret ->
        val step1 = secret.mix(secret * 64).prune()
        val step2 = step1.mix(step1 / 32).prune()
        val step3 = step2.mix(step2 * 2048).prune()
        step3
    }


    override fun solve2(data: List<Long>): Long {
        return buildMap {
            data
                .map { generateSecrets(it).take(2001).map { i -> (i % 10).toInt() }.toList() }
                .forEach { s ->
                    s
                        .windowed(5, 1)
                        .map { it.zipWithNext { first, second -> second - first } to it.last() }
                        .distinctBy { it.first }
                        .forEach { (key, value) -> this[key] = (this[key] ?: 0L) + value.toLong()}
                }
        }.maxOf {it.value}

    }
}