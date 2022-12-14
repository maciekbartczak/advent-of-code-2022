import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Int {
        val grid = parseInput(input,  false)
        return getGrainsWithinBoundsCount(grid)
    }

    fun part2(input: List<String>): Int {
        val grid = parseInput(input, true)
        return getGrainsWithinBoundsCount(grid) + 1
    }

    val testInput = readInput("Day14_test")
//    check(part1(testInput) == 24)
    check(part2(testInput) == 93)

    val input = readInput("Day14")
//    println(part1(input))
    println(part2(input))
}

private fun parseInput(input: List<String>, addFloor: Boolean): MutableList<MutableList<Char>> {
    val paths = input.flatMap {
        val coords = it.split(" -> ")
        coords.zipWithNext().map { pair ->

            val pairs = listOf(pair.first.getCoords(), pair.second.getCoords())
                .sortedWith(compareBy(
                    { coords -> coords.first },
                    { coords -> coords.second }
                ))
            val first = pairs.first()
            val second = pairs.last()
            first.first..second.first to first.second..second.second
        }
    }
    return paths.toGrid(addFloor)
}

private fun getGrainsWithinBoundsCount(grid: MutableList<MutableList<Char>>): Int {
    var withinBounds: Boolean
    var counter = 0
    do {
        withinBounds = grid.simulateSand()
        if (withinBounds) {
            counter++
        }
    } while (withinBounds)
    grid.prettyPrint()
    return counter
}

private fun String.getCoords(): Pair<Int, Int> {
    val split = this.split(",")
    return split[0].toInt() to split[1].toInt()
}

private fun List<Pair<IntRange, IntRange>>.toGrid(addWall: Boolean): MutableList<MutableList<Char>> {
    // this might be a bit hacky, but I think that we can just simulate infinity with a big enough number
    // due to the paths being limited horizontally with their coordinates
    val infinity = 1000
    val minX = this
        .map { it.first.first }
        .filter { it> 0 }
        .min()
    val maxX = max(this.maxOf { it.first.last }, infinity)
    var maxY = this.maxOf { it.second.last }

    var pairs = this
    if (addWall) {
        maxY += 2
        pairs = pairs + (-infinity + minX..infinity to maxY..maxY)
    }

    val grid = mutableListOf<MutableList<Char>>()

    val startX = min(-infinity, 0)
    for (y in 0..maxY) {
        val row = mutableListOf<Char>()
        for (x in startX..maxX - minX) {
            row.add('.')
        }
        grid.add(row)
    }

    pairs.forEach {
        for (x in it.first.first..it.first.last) {
            for (y in it.second.first..it.second.last) {
                val gridX = x - minX - startX
                grid[y][gridX] = '#'
            }
        }
    }
    grid[0][500 - minX - startX] = '+'
    return grid
}

private fun List<List<Char>>.prettyPrint() {
    this.forEach { row ->
        row.forEach { print(it) }
        println()
    }
}

private fun List<MutableList<Char>>.simulateSand(): Boolean {
    val sandSourceX = this.first().indexOf('+')
    var currentSandPosition = sandSourceX to 0
    var moving = true

    while (moving) {
        moving = false

        for (sandOffset in sandOffsets) {
            val newSandPosition = currentSandPosition.offsetBy(sandOffset)
            if (!this.isWithinBounds(newSandPosition)) {
                return false
            }
            if (this.moveSand(currentSandPosition, newSandPosition, sandSourceX) != currentSandPosition) {
                currentSandPosition = newSandPosition
                moving = true
                break
            }
        }
    }
    if (this.isSandSource(currentSandPosition)) {
        return false
    }
    return true
}

private fun List<MutableList<Char>>.moveSand(
    currentSandPosition: Pair<Int, Int>, nextSandPosition: Pair<Int, Int>, sourceX: Int
): Pair<Int, Int> {
    if (this[nextSandPosition.second][nextSandPosition.first] != '.') {
        return currentSandPosition
    }
    this[nextSandPosition.second][nextSandPosition.first] = 'o'
    // do not remove the sand source from grid for drawing purposes
    if (currentSandPosition.first == sourceX && currentSandPosition.second == 0) {
        this[currentSandPosition.second][currentSandPosition.first] = '+'
    } else {
        this[currentSandPosition.second][currentSandPosition.first] = '.'
    }

    return nextSandPosition
}

private fun List<MutableList<Char>>.isWithinBounds(coords: Pair<Int, Int>): Boolean {
    return coords.second in this.indices && coords.first in this[0].indices
}

private fun List<MutableList<Char>>.isSandSource(coords: Pair<Int, Int>): Boolean {
    return this[coords.second][coords.first] == '+'
}

val sandOffsets = listOf(
    0 to 1,
    -1 to 1,
    1 to 1
)
