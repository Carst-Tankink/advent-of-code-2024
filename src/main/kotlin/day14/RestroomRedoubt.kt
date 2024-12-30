package day14

import util.Point
import util.Solution



fun Point.wrap(width: Int, height: Int): Point {
    val nextX = x % width
    val nextY = y % height
    return Point(
        if (nextX < 0) nextX + width else nextX,
        if (nextY < 0) nextY + height else nextY
    )
}
data class Robot(
    val position: Point,
    val velocity: Point
)

class RestroomRedoubt(fileName: String?) : Solution<Robot, Int>(fileName) {
    override fun parse(line: String): Robot {
        val (posLine, velLine) = line.split(" ")

        val pos = posLine.substringAfter("=").split(",").map { it.toLong() }.let {
            Point(it[0], it[1])
        }

        val vel = velLine.substringAfter("=").split(",").map { it.toLong() }.let {
            Point(it[0], it[1])
        }

        return Robot(pos, vel)
    }

    override fun solve1(data: List<Robot>): Int {
        return calculateSafetyFactor(101, 103)
    }

    fun calculateSafetyFactor(width: Int, height: Int): Int {
        fun stepRobot(robot: Robot, steps: Int): Robot {
            return robot.copy(position = (robot.position + (robot.velocity * steps)).wrap(width, height))
        }

        val robots = data.map { stepRobot(it, 100) }

        val midpoint = Point(width / 2, height / 2)

        val quadrants = listOf(
            robots.filter { it.position.x < midpoint.x && it.position.y < midpoint.y }, // Top-left
            robots.filter { it.position.x > midpoint.x && it.position.y < midpoint.y }, // Top-right
            robots.filter { it.position.x > midpoint.x && it.position.y > midpoint.y }, // Bottom-right
            robots.filter { it.position.x < midpoint.x && it.position.y > midpoint.y }, // Bottom-left
        )


        return quadrants.map { it.count() }.reduce(Int::times)
    }

    override fun solve2(data: List<Robot>): Int {
        TODO("Not yet implemented")
    }

}