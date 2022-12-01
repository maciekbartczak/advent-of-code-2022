fun main() {
    fun part1(input: String): Int {
        return input
            .split("\n\n")
            .maxOf { calories ->
                calories
                    .split("\n")
                    .sumOf { it.toInt() }
            }
    }

    fun part2(input: String): Int {
        return input
            .split("\n\n")
            .map { calories ->
                calories
                    .split("\n")
                    .sumOf { it.toInt() }
            }
            .sortedDescending()
            .take(3)
            .sum()
    }

    val testInput = readInputAsString("Day01_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInputAsString("Day01")
    println(part1(input))
    println(part2(input))
}
