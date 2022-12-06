fun main() {
    fun part1(input: String): Int {
        return findMarkerEndIndex(input, 4)
    }

    fun part2(input: String): Int {
       return findMarkerEndIndex(input, 14)
    }

    val testInput = readInputAsString("Day06_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 19)

    val input = readInputAsString("Day06")
    println(part1(input))
    println(part2(input))
}

private fun findMarkerEndIndex(input: String, markerLength: Int): Int {
    return input
        .windowed(markerLength)
        .indexOfFirst {
            it.length == it.toSet().size
        }
        .plus(markerLength)
}

