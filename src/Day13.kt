// tbh I gave up on this one, solution heavily based on:
// https://github.com/ash42/adventofcode/tree/main/adventofcode2022/src/nl/michielgraat/adventofcode2022/day13
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Int {
        return input
            .chunked(3)
            .map {
                (first, second, _) -> Packet(first) to Packet(second)
            }.mapIndexed { index, pair ->
                var result = 0
                if (pair.first < pair.second) {
                    result = index + 1
                }
                result
            }.sum()
    }

    fun part2(input: List<String>): Int {
        val dividerPackets = listOf(
            "[[2]]",
            "[[6]]",
            ""
        )
        val combinedInput = input + dividerPackets
        val packets = combinedInput
            .filter { it.isNotEmpty() }
            .map { Packet(it) }
            .sorted()
        val firstDividerIndex = packets.indexOfFirst { it.content == "[[2]]" }
        val secondDividerIndex = packets.indexOfFirst { it.content == "[[6]]" }
        return (firstDividerIndex + 1) * (secondDividerIndex + 1)
    }

    val testInput = readInput("Day13_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 140)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}


private class Packet(
    input: String,
): Comparable<Packet> {
    private var value = 0
    private var isList = false
    private val childPackets = mutableListOf<Packet>()
    val content: String

    init {
        content = input
        if (!input.startsWith('[')) {
             value = input.toInt()
        } else {
            parseList(input)
        }
    }

    private fun parseList(input: String) {
        isList = true
        val packetContent = input.substring(1, input.length - 1)
        var depth = 0
        val currentPacket = StringBuilder()
        packetContent.forEach {
            // [1],[2,3,4]
            if (it == ',' && depth == 0) {
                childPackets.add(Packet(currentPacket.toString()))
                currentPacket.clear()
            } else {
                when (it) {
                    '[' -> depth++
                    ']' -> depth--
                }
                currentPacket.append(it)
            }
        }
        if (currentPacket.isNotEmpty()) {
            childPackets.add(Packet(currentPacket.toString()))
        }
    }

    override fun compareTo(other: Packet): Int {
        if (!this.isList && !other.isList) {
            return this.value.compareTo(other.value)
        }
        if (this.isList && other.isList) {
            val myChildrenSize = this.childPackets.size
            val otherChildrenSize = other.childPackets.size

            for (i in 0 until  min(myChildrenSize, otherChildrenSize)) {
                val result = this.childPackets[i].compareTo(other.childPackets[i])
                if (result != 0) {
                    return result
                }
            }
            return myChildrenSize.compareTo(otherChildrenSize)
        }
        if (this.isList && !other.isList) {
            return this.compareTo(Packet("[${other.value}]"))
        }
        return Packet("[${this.value}]").compareTo(other)
    }


}