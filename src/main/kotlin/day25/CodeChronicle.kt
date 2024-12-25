package day25

import util.Helpers.Companion.transpose
import util.Solution

class CodeChronicle(fileName: String?) : Solution<List<Char>, Int>(fileName) {
    override fun parse(line: String): List<Char> {
        return line.toList()
    }

    override fun solve1(data: List<List<Char>>): Int {
        val blocks = parseBlocks(data, emptyList(), emptyList())

        val (locks, keys) = blocks.partition { b -> b.first().all { it == '#' } }

        val lockHeights = locks.map { lock ->
            lock.transpose()
                .map { l -> l.count { it == '#' } - 1 }
        }

        val keyHeights = keys.map { key ->
            key.transpose()
                .map { l -> l.count { it == '#' } - 1 }
        }


        return lockHeights.flatMap { lock ->
            keyHeights
                .map { key ->
                    lock.zip(key) { l, k -> l + k }
                }
        }.count { it.all { h -> h < 6 } }
    }


    override fun solve2(data: List<List<Char>>): Int {
        TODO("Not yet implemented")
    }

    private tailrec fun parseBlocks(
        data: List<List<Char>>,
        currentBlock: List<List<Char>>,
        acc: List<List<List<Char>>>
    ): List<List<List<Char>>> {
        return if (data.isEmpty()) acc + listOf(currentBlock) else {
            val next = data.first()
            val tail = data.drop(1)

            if (next.isEmpty()) {
                parseBlocks(tail, emptyList(), acc + listOf(currentBlock))
            } else {
                parseBlocks(tail, currentBlock + listOf(next), acc)
            }
        }
    }
}