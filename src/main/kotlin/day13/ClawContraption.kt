package day13

import util.Solution

sealed interface MachineLine
data class Button(val name: String, val x: Long, val y: Long) : MachineLine
data class Prize(val x: Long, val y: Long) : MachineLine

data class Machine(
    val aX: Long, val aY: Long,
    val bX: Long, val bY: Long,
    val prizeX: Long, val prizeY: Long
)

private val BUTTON_REGEX = "Button (\\w): X\\+(\\d+), Y\\+(\\d+)".toRegex()
private val PRIZE_REGEX = "Prize: X=(\\d+), Y=(\\d+)".toRegex()

class ClawContraption(fileName: String?) : Solution<MachineLine, Long>(fileName) {
    override fun parse(line: String): MachineLine? {
        return when {
            BUTTON_REGEX.matches(line) -> BUTTON_REGEX.find(line)?.groupValues?.let {
                Button(it[1], it[2].toLong(), it[3].toLong())

            }

            PRIZE_REGEX.matches(line) -> PRIZE_REGEX.find(line)?.groupValues?.let {
                Prize(it[1].toLong(), it[2].toLong())
            }

            else -> null

        }
    }

    private fun makeMachine(lines: List<MachineLine>): Machine {
        val a = lines[0] as Button
        val b = lines[1] as Button
        val prize = lines[2] as Prize

        return Machine(a.x, a.y, b.x, b.y, prize.x, prize.y)
    }

    private tailrec fun buildMachines(
        lines: List<MachineLine>,
        currentMachine: List<MachineLine>,
        machines: List<Machine>
    ): List<Machine> {
        return if (lines.isEmpty()) {
            machines
        } else {
            val head = lines.first()
            val tail = lines.drop(1)
            if (head is Prize) {
                buildMachines(
                    tail,
                    emptyList(),
                    machines + listOf(makeMachine(currentMachine + head))
                )
            } else {
                buildMachines(
                    tail,
                    currentMachine + head,
                    machines
                )
            }
        }
    }

    private fun Machine.calculateTokens(): Long {
        /* We're looking for the solution of the system
                aX * aN + bX * bN = pX
                aY * aN + bY * bN = pY
           Where aX aY, bX, bY, px, pY are constant
           We can use Cramer's rule here, to solve the 2x2 system
         */
        val det = aX * bY - aY * bX
        val a = (prizeX * bY - prizeY * bX) / det
        val b = (aX * prizeY - aY * prizeX) / det

        return if (a * aX + b * bX == prizeX && a * aY + b * bY == prizeY) a * 3 + b else 0
    }

    override fun solve1(data: List<MachineLine>): Long {

        return buildMachines(data, emptyList(), emptyList()).sumOf {
            it.calculateTokens()
        }
    }

    override fun solve2(data: List<MachineLine>): Long {
        val correctedData = data.map {
            if (it is Prize) {
                it.copy(x = it.x + 10000000000000, y = it.y + 10000000000000)
            } else it
        }

        return buildMachines(correctedData, emptyList(), emptyList()).sumOf {
            it.calculateTokens()
        }
    }
}