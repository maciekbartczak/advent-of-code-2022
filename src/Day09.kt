import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        return getUniquePositionsVisitedByTailCount(input, 2)
    }

    fun part2(input: List<String>): Int {
        return getUniquePositionsVisitedByTailCount(input, 10)
    }

    val testInput = readInput("Day09_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 1)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}

val directionMap = mapOf(
    'U' to (0 to 1),
    'D' to (0 to -1),
    'L' to (-1 to 0),
    'R' to (1 to 0)
)

private fun getUniquePositionsVisitedByTailCount(moves: List<String>, knotCount: Int): Int {
    val visitedPositions = mutableSetOf<Pair<Int, Int>>()
    val knotPositions = mutableListOf<Pair<Int, Int>>()
    repeat(knotCount) {
        knotPositions.add(0 to 0)
    }
    moves
        .map {
            it.parseInput()
        }
        .forEach {
            val offset = directionMap[it.first]!!
            repeat(it.second) {
                // treat the knot under index 0 as head
                knotPositions[0] = knotPositions[0].offsetBy(offset)
                var lastOffset = offset
                for (i in 1 until knotCount) {
                    val head = knotPositions[i - 1]
                    val currentKnot = knotPositions[i]

                    val (newTail, tailMoveOffset) = moveTail(currentKnot, head, lastOffset)
                    knotPositions[i] = newTail
                    lastOffset = tailMoveOffset
                }
                visitedPositions.add(knotPositions[knotCount - 1])
            }
        }
    return visitedPositions.size
}

private fun String.parseInput(): Pair<Char, Int> {
    val input = this.split(" ")
    return input[0].toCharArray()[0] to input[1].toInt()
}

private fun moveTail(
    currentTailPosition: Pair<Int, Int>,
    currentHeadPosition: Pair<Int, Int>,
    lastHeadMove: Pair<Int, Int>
): Pair<Pair<Int, Int>, Pair<Int, Int>> {
    if (currentHeadPosition.isTouching(currentTailPosition)) {
        return currentTailPosition to (0 to 0)
    }
    if (currentHeadPosition.isAlignedVertically(currentTailPosition)) {
        val offset = 0 to lastHeadMove.second
        return currentTailPosition.offsetBy(offset) to offset
    }
    if (currentHeadPosition.isAlignedHorizontally(currentTailPosition)) {
        val offset = lastHeadMove.first to 0
        return currentTailPosition.offsetBy(offset) to offset
    }

    val xDistance = abs(currentHeadPosition.first - currentTailPosition.first)
    val yDistance = abs(currentHeadPosition.second - currentTailPosition.second)
    val offset = if (xDistance > 1 && yDistance > 1) {
        lastHeadMove
    } else if (xDistance > 1) {
        lastHeadMove.first to currentHeadPosition.second - currentTailPosition.second
    } else {
        currentHeadPosition.first - currentTailPosition.first to lastHeadMove.second
    }
    return currentTailPosition.offsetBy(offset) to offset

}

private fun Pair<Int, Int>.isAlignedVertically(other: Pair<Int, Int>): Boolean {
    return this.first == other.first
}

private fun Pair<Int, Int>.isAlignedHorizontally(other: Pair<Int, Int>): Boolean {
    return this.second == other.second
}

private fun Pair<Int, Int>.isTouching(other: Pair<Int, Int>): Boolean {
    for (x in -1..1) {
        for (y in -1..1) {
            val offset = x to y
            if (this.offsetBy(offset) == other) {
                return true
            }
        }
    }
    return false
}


