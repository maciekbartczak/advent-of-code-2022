fun main() {
    fun part1(input: List<String>): Int {
        return input
            .map { getRoundInput(it) }
            .sumOf { calculateScore(it.first, it.second) }
    }

    fun part2(input: List<String>): Int {
        return input
            .map { getRoundInput(it) }
            .sumOf {
                calculateScore(it.first, getPlayerPick(it.first, it.second))
            }
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}

fun calculateScore(opponent: String, player: String): Int {
    var score = shapeScores[player]!!
    if (matchingShape[opponent] == player) {
        score += 3
    } else if (shapeBeatenBy[opponent] == player) {
        score += 6
    }
    return score
}

fun getRoundInput(line: String): Pair<String, String> {
    val picks = line.split(" ")

    return picks[0] to picks[1]
}

fun getPlayerPick(opponentShape: String, outcome: String): String {
    return when (outcome) {
        "X" -> shapeBeats[opponentShape]!!
        "Y" -> matchingShape[opponentShape]!!
        "Z" -> shapeBeatenBy[opponentShape]!!
        else -> ""
    }
}

val shapeScores = mapOf(
    "X" to 1,
    "Y" to 2,
    "Z" to 3
)

val shapeBeatenBy = mapOf(
    "A" to "Y",
    "B" to "Z",
    "C" to "X"
)

val shapeBeats = mapOf(
    "A" to "Z",
    "B" to "X",
    "C" to "Y"
)

val matchingShape = mapOf(
    "A" to "X",
    "B" to "Y",
    "C" to "Z"
)
