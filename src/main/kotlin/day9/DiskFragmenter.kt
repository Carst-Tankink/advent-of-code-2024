package day9

import util.Solution

class DiskFragmenter(fileName: String?) : Solution<List<Long?>, Long>(fileName) {
    override fun parse(line: String): List<Long?> {
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
                        checksum + (b * (targetBlock ?: idx))
                    )
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

    data class Block(
        val start: Int,
        val length: Int,
        val fileId: Long?,
    ) {
        fun checksum(index: Int = start): Long {
            return (0..<length).sumOf {
                (index + it) * (fileId ?: 0)
            }
        }
    }

    private tailrec fun findBlocks(
        list: List<IndexedValue<Long?>>,
        currentBlock: Block?,
        blocks: List<Block>
    ): List<Block> {
        return if (list.isEmpty()) {
            blocks + (currentBlock?.let { listOf(it) } ?: emptyList())
        } else {
            val head = list.first()
            val tail = list.drop(1)

            when {
                currentBlock == null -> {
                    findBlocks(tail, Block(head.index, 1, head.value), blocks)
                }

                currentBlock.fileId == head.value -> {
                    findBlocks(tail, currentBlock.copy(length = currentBlock.length + 1), blocks)
                }

                else -> {
                    findBlocks(
                        tail,
                        Block(head.index, 1, head.value),
                        blocks + currentBlock
                    )
                }
            }

        }
    }

    override fun solve2(data: List<List<Long?>>): Long {
        val fileData = data.first().withIndex().toList()

        val blocks = findBlocks(fileData, null, emptyList())
        val emptyBlocks = blocks.filter { it.fileId == null }.withIndex().toList()

        tailrec fun calculateChecksum(
            blocks: List<Block>,
            emptyBlocks: List<IndexedValue<Block>>,
            checksum: Long
        ): Long {
            return if (blocks.isEmpty()) checksum else {
                val block = blocks.first()
                val tail = blocks.drop(1)

                return if (block.fileId != null) {
                    val emptyBlock = emptyBlocks.firstOrNull { it.value.length >= block.length }

                    if (emptyBlock == null) {
                        calculateChecksum(tail, emptyBlocks, checksum + block.checksum())
                    } else {
                        val newBlock = emptyBlock.value.copy(
                            length = emptyBlock.value.length - block.length,
                            start = emptyBlock.value.start + block.length
                        )

                        val newEmpties = emptyBlocks.map {
                            if (it.index == emptyBlock.index) IndexedValue(
                                it.index,
                                newBlock
                            ) else it
                        }
                        calculateChecksum(tail, newEmpties, checksum + block.checksum(emptyBlock.value.start))
                    }

                } else {
                    calculateChecksum(tail, emptyBlocks.dropLast(1), checksum)
                }
            }
        }

        return calculateChecksum(blocks.reversed(), emptyBlocks, 0L)
    }
}