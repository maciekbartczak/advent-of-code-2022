fun main() {
    fun part1(input: List<String>): Int {
        return input
            .map {
                val compartments = it.chunked(it.length / 2)
                findDuplicateCharacter(compartments[0] to compartments[1])
            }
            .sumOf { calculatePriority(it) }
    }

    fun part2(input: List<String>): Int {
        return input
            .chunked(3)
            .map {
                val firstDuplicates = findAllDuplicateCharacters(it[0] to it[1])
                val secondDuplicates = findAllDuplicateCharacters(it[1] to it[2])
                findDuplicateCharacter(
                    firstDuplicates.joinToString("") to secondDuplicates.joinToString("")
                )
            }.sumOf { calculatePriority(it) }
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}

fun findDuplicateCharacter(strings: Pair<String, String>): Char {
    val first = strings.first
    val second = strings.second

    first.forEach {
        if (second.contains(it)) {
            return it
        }
    }

    return '-'
}

fun findAllDuplicateCharacters(strings: Pair<String, String>): Set<Char> {
    val first = strings.first
    val second = strings.second
    val duplicates = mutableSetOf<Char>()

    first.forEach {
        if (second.contains(it)) {
            duplicates.add(it)
        }
    }

    return duplicates
}

fun calculatePriority(c: Char): Int {
    return when {
        c.isLowerCase() -> c.code - 96
        c.isUpperCase() -> c.code - 38
        else -> 0
    }
}