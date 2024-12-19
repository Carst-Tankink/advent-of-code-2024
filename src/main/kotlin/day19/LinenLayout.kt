package day19

import util.Solution

sealed interface Linen

data class Pattern(val pattern: List<String>) : Linen
data class Design(val design: String) : Linen

class LinenLayout(fileName: String?) : Solution<Linen, Long>(fileName) {
    override fun parse(line: String): Linen? {
        return when {
            line.isEmpty() -> null
            line.contains(",") -> Pattern(line.split(", "))
            else -> Design(line)
        }
    }

    override fun solve1(data: List<Linen>): Long {
        val patterns = data.filterIsInstance<Pattern>().first().pattern
        return data
            .filterIsInstance<Design>()
            .map { it.design }
            .count { countPatterns(patterns, it) > 0 }
            .toLong()
    }

    override fun solve2(data: List<Linen>): Long {
        val patterns = data.filterIsInstance<Pattern>().first().pattern
        return data
            .filterIsInstance<Design>()
            .map { it.design }
            .sumOf { countPatterns(patterns, it) }
    }

    private fun countPatterns(patterns: List<String>, designs: String): Long {
        val partialSolutions: MutableMap<String, Long> = mutableMapOf()
        fun canBeMade(design: String): Long {
            return if (design in partialSolutions) {
                partialSolutions[design]!!
            } else {
                if (design.isEmpty()) {
                    partialSolutions[design] = 1L
                    1L
                } else {
                    val startPatterns = patterns.filter { design.startsWith(it) }
                    if (startPatterns.isEmpty()) {
                        partialSolutions[design] = 0L
                        0L
                    } else {
                        val x = startPatterns.sumOf {
                            canBeMade(design.drop(it.length))
                        }
                        partialSolutions[design] = x
                        x
                    }
                }
            }
        }
        return canBeMade(designs)
    }
}