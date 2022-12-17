import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.system.measureTimeMillis

fun main() {
    fun part1(input: List<String>, height: Int): Int {
        val (sensors, beacons) = parseInput(input)

        val sensorRanges = sensors
            .map { it.getSensorXRange(height) }

        val start = sensorRanges.minOf { it.first }
        val end = sensorRanges.maxOf { it.last }
        val beaconsAtHeight = beacons.getCountAtHeight(height)

        // +1 because of the x=0
        return abs(start - end) + 1 - beaconsAtHeight
    }

    fun part2(input: List<String>, rangeLimit: Int): Long {
        val (sensors, _) = parseInput(input)

        // because there is only one uncovered point it must be somewhere at sensorDistance + 1
        // that way we can greatly reduce the number of points to check since sensors number is quite low (23)
        val distressBeacon = sensors
            .flatMap { it.getEdgePoints(rangeLimit) }
            .first { candidate -> sensors.none { it.isPositionWithinRange(candidate) } }
        return distressBeacon.first * 4000000L + distressBeacon.second
    }

    val testInput = readInput("Day15_test")
    check(part1(testInput, 10) == 26)
    check(part2(testInput, 20) == 56000011L)

    val input = readInput("Day15")
    println(part1(input, 2000000))
    val part2Time = measureTimeMillis {
        println(part2(input, 4000000))
    }
    println("part 2 took ${part2Time}ms")
}

private class Sensor(
    val position: Pair<Int, Int>,
    val range: Int
) {
    companion object {
        fun of(sensorPosition: Pair<Int, Int>, beaconPosition: Pair<Int, Int>): Sensor {
            return Sensor(
                sensorPosition,
                sensorPosition.manhattanDistance(beaconPosition)
            )
        }
    }

    fun isPositionWithinRange(position: Pair<Int, Int>): Boolean {
        return position.first in getSensorXRange(position.second)
    }

    fun getSensorXRange(y: Int): IntRange {
        val yDiff = abs(position.second - y)
        if (yDiff > range) {
            return IntRange.EMPTY
        }
        val range = range - yDiff
        return position.first - range..position.first + range
    }

    fun getEdgePoints(limit: Int): List<Pair<Int, Int>> {
        val points = mutableListOf<Pair<Int, Int>>()
        val yStart = max(min(position.second - range - 1, limit), 0)
        val yEnd = max(min(position.second + range + 1, limit), 0)
        for (y in yStart..yEnd) {
            val xRange = getSensorXRange(y)

            if (xRange.isEmpty()) {
                points.add(position.first to y)
            } else {
                if (xRange.first in 1..limit) {
                    points.add(xRange.first - 1 to y)
                }
                if (xRange.last in -1..limit) {
                    points.add(xRange.last + 1 to y)
                }
            }
        }
        return points
    }
}

private fun Pair<Int, Int>.manhattanDistance(other: Pair<Int, Int>): Int {
    return abs(this.first - other.first) + abs(this.second - other.second)
}

private fun String.extractPositions(): Pair<Pair<Int, Int>, Pair<Int, Int>> {
    val pattern = """x=(-?\d+), y=(-?\d+).*x=(-?\d+), y=(-?\d+)"""
    val match = pattern.toRegex().find(this) ?: throw IllegalStateException()

    val sensorPosition = match.groupValues[1].toInt() to match.groupValues[2].toInt()
    val beaconPosition = match.groupValues[3].toInt() to match.groupValues[4].toInt()

    return sensorPosition to beaconPosition
}

private fun Set<Pair<Int, Int>>.getCountAtHeight(height: Int): Int {
    return this.count { it.second == height }
}

private fun parseInput(input: List<String>): Pair<List<Sensor>, Set<Pair<Int, Int>>> {
    val sensors = mutableListOf<Sensor>()
    val beacons = mutableSetOf<Pair<Int, Int>>()

    input.forEach {
        val (sensor, beacon) = it.extractPositions()
        sensors.add(Sensor.of(sensor, beacon))
        beacons.add(beacon)
    }
    return sensors to beacons
}