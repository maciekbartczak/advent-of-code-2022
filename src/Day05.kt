import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.text.StringBuilder

fun main() {
    fun part1(input: String): String {
        return getTopCratesAfterRearrangement(input, false)
    }

    fun part2(input: String): String {
        return getTopCratesAfterRearrangement(input, true)
    }

    val testInput = readInputAsString("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInputAsString("Day05")
    println(part1(input))
    println(part2(input))
}

private fun getTopCratesAfterRearrangement(input: String, maintainCrateOrder: Boolean): String {
    val (crates, instructions) = parseInput(input)
    val stacks = parseCrates(crates)
    executeInstructions(stacks, instructions, maintainCrateOrder)

    val result = StringBuilder()
    stacks.forEach {
        result.append(it.last())
    }
    return result.toString()
}
private fun parseInput(input: String): Pair<String, String> {
    val split = input.split("\n\n")
    return split[0] to split[1]
}

private fun parseCrates(crates: String): List<Stack<Char>> {
    val crateLines = crates.split("\n")
    val stackCount = crateLines.last().last().digitToInt()

    val stacks = mutableListOf<Stack<Char>>()
    for (i in 0 until stackCount) {
        stacks.add(Stack())
    }

    for (i in crateLines.size - 1 downTo 0) {
        for (j in 1 until crateLines[i].length step 4) {
            val crate = crateLines[i][j]
            val stackIndex = j / 4
            if (crate.isLetter()) {
                stacks[stackIndex].push(crate)
            }
        }
    }

    return stacks
}

private fun executeInstructions(
    stacks: List<Stack<Char>>, instructions: String,
    maintainCrateOrder: Boolean
) {
    instructions
        .split("\n")
        .forEach {
            val (count, source, target) = it.parseInstruction()
            val cratesToMove = ArrayDeque<Char>()
            repeat(count) { cratesToMove.add(stacks[source].pop()) }
            repeat(count) {
                val currentCrate = if (maintainCrateOrder)
                    cratesToMove.removeLast()
                else
                    cratesToMove.removeFirst()
                stacks[target].push(currentCrate)
            }
        }
}


private fun String.parseInstruction(): Triple<Int, Int, Int> {
    val scanner = Scanner(this).useDelimiter("\\D+")
    return Triple(scanner.nextInt(), scanner.nextInt() - 1, scanner.nextInt() - 1)
}