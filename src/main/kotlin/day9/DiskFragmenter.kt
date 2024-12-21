package day9

import util.Solution

class DiskFragmenter(fileName: String?) : Solution<List<Long?>, Long>(fileName) {
    override fun parse(line: String): List<Long?>? {
        return line.windowed(2, 2, true)
            .withIndex()
            .flatMap { (idx, value) ->
                List(value.first().digitToInt()) { _ -> idx.toLong() } +
                        List(value.getOrElse(1) { '0' }.digitToInt()) { _ -> null }
            }
    }

    override fun solve1(data: List<List<Long?>>): Long {
        tailrec fun calculateChecksum(blocks: List<IndexedValue<Long?>>, emptyBlocks: List<Int>, checksum: Long): Long {
            return if (blocks.isEmpty()) checksum else {
                val (idx, b) = blocks.first()
                if (b != null) {
                    val targetBlock = emptyBlocks.firstOrNull()
                    calculateChecksum(
                        blocks.drop(1),
                        emptyBlocks.drop(1),
                        checksum + (b * (targetBlock ?: idx)))
                } else {
                    calculateChecksum(
                        blocks.drop(1),
                        emptyBlocks.dropLast(1),
                        checksum
                    )
                }
            }
        }

        val disk = data.first()
        val empty = disk.indices.filter { disk[it] == null }
        return calculateChecksum(disk.withIndex().reversed(), empty, 0)
    }

    override fun solve2(data: List<List<Long?>>): Long {
        TODO("Not yet implemented")
    }
}