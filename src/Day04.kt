fun main() {
    fun part1(input: List<String>): Int {
        return input
            .map {
                parsePair(it)
            }
            .sumOf {
                checkFullOverlap(it)
            }
    }

    fun part2(input: List<String>): Int {
        return input
            .map {
                parsePair(it)
            }.sumOf {
                checkPartialOverlap(it)
            }
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}

private fun parsePair(line: String): Pair<String, String> {
    val pair = line.split(",")
    return pair[0] to pair[1]
}

private fun checkFullOverlap(pair: Pair<String, String>): Int {
    val firstRange = pair.first.toRange()
    val secondRange = pair.second.toRange()

    if (firstRange.overlaps(secondRange) || secondRange.overlaps(firstRange)) {
        return 1
    }
    return 0
}

private fun checkPartialOverlap(pair: Pair<String, String>): Int {
    val firstRange = pair.first.toRange()
    val secondRange = pair.second.toRange()

    if ((firstRange intersect secondRange).isNotEmpty()) {
        return 1
    }
    return 0
}

private fun String.toRange(): IntRange {
    val range = this.split("-")
        .map { it.toInt() }
    return IntRange(range[0], range[1])
}

private fun IntRange.overlaps(other: IntRange): Boolean {
    return this.first >= other.first && this.last <= other.last
}