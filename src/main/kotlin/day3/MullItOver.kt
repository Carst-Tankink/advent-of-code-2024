package day3

import util.Solution

enum class ParserState {
    IDLE,
    FIRST_NUMBER,
    SECOND_NUMBER,
    DISABLED
}

class MullItOver(fileName: String?) : Solution<String, Long>(fileName) {
    override fun parse(line: String): String {
        return line
    }

    override fun solve1(data: List<String>): Long {
        return data.sumOf { processLine(it, 0L, ParserState.IDLE) }
    }

    override fun solve2(data: List<String>): Long {
        val line = data.joinToString("")
        val strippedLine = stripLine(line, "")
        return processLine(strippedLine, 0L, ParserState.IDLE)
    }

    private tailrec fun stripLine(remaining: String, acc: String): String {
        val dontStart = remaining.indexOf("don't()")
        return if (dontStart < 0) acc + remaining else {
            val start = remaining.take(dontStart)
            val tail = remaining.drop(dontStart + "don't()".length)
            val pickup = tail.indexOf("do()")
            val continued = if (pickup > 0) tail.drop(pickup + "do()".length) else ""
            stripLine(continued, acc + start + "X")
        }
    }

    private tailrec fun processLine(
        remaining: String,
        acc: Long,
        parserState: ParserState,
        firstNumber: Int? = null
    ): Long {
        val mul = "mul("
        return if (remaining.isEmpty()) acc else {
            when (parserState) {
                ParserState.IDLE -> {
                    val start = remaining.dropWhile { it != 'm' }
                    if (start.take(mul.length) == mul) {
                        processLine(start.drop(mul.length), acc, ParserState.FIRST_NUMBER)
                    } else {
                        processLine(start.drop(1), acc, ParserState.IDLE)
                    }
                }

                ParserState.FIRST_NUMBER -> {
                    val number = remaining.takeWhile { it.isDigit() }
                    val tail = remaining.drop(number.length)
                    if (number.isNotEmpty() && tail.startsWith(',')) {
                        processLine(remaining.drop(number.length + 1), acc, ParserState.SECOND_NUMBER, number.toInt())
                    } else {
                        processLine(remaining.drop(1), acc, ParserState.IDLE)
                    }
                }

                ParserState.SECOND_NUMBER -> {
                    val number = remaining.takeWhile { it.isDigit() }
                    val tail = remaining.drop(number.length)
                    if (number.isNotEmpty() && tail.startsWith(')')) {
                        processLine(
                            remaining.drop(number.length + 1),
                            acc + (firstNumber!! * number.toInt()),
                            ParserState.IDLE,
                            null
                        )
                    } else {
                        processLine(remaining.drop(1), acc, ParserState.IDLE)
                    }
                }

                ParserState.DISABLED -> error("Should never happen")
            }
        }
    }
}