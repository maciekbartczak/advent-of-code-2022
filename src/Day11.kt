fun main() {
    fun part1(input: String): Long {
        return simulateMonkeys(input, 20, true)
    }

    fun part2(input: String): Long {
       return simulateMonkeys(input, 10000, false)
    }

    val testInput = readInputAsString("Day11_test")
    check(part1(testInput) == 10605L)
    check(part2(testInput) == 2713310158)

    val input = readInputAsString("Day11")
    println(part1(input))
    println(part2(input))
}

private fun simulateMonkeys(input: String, nRounds: Int, inspectionAffectsWorryLevel: Boolean): Long {
    val monkeyLines = input.split("\n\n")
    val monkeys = monkeyLines.
        map { Monkey(it, inspectionAffectsWorryLevel) }
    val supermodulo = monkeys
        .map { it.testNumber.toLong() }
        .reduce { acc, i -> acc * i }

    repeat(nRounds) {
        processRound(monkeys, supermodulo)
    }
    return monkeys
        .map { it.inspectedTimes }
        .sortedDescending()
        .take(2)
        .reduce { acc, i -> acc * i}
}
private fun processRound(monkeys: List<Monkey>, supermodulo: Long) {
    monkeys.forEach {
        val toRemove = mutableListOf<Long>()
        for (i in it.items.indices) {
            val (item, target) = it.getThrowTarget(i, supermodulo)
            monkeys[target].items.add(item)
            toRemove.add(item)
        }
        it.items.removeAll { value -> value in toRemove }
        toRemove.clear()
    }
}
private class Monkey(
    monkey: String,
    val inspectionAffectsWorryLevel: Boolean
) {
    val items = mutableListOf<Long>()
    val operation: (old: Long) -> Long
    val outcomes: List<Int>
    val testNumber: Long
    var inspectedTimes: Long = 0

    init {
        val lines = monkey.split("\n")
        items.addAll(parseItems(lines[1]))
        operation = parseOperation(lines[2])
        testNumber = parseTest(lines[3])
        outcomes = parseOutcomes(lines[4], lines[5])
    }

    private fun parseOutcomes(ifTrue: String, ifFalse: String): List<Int> {
        return listOf(
            ifTrue.last().digitToInt(),
            ifFalse.last().digitToInt()
        )
    }

    private fun parseTest(line: String): Long {
        val startIndex = line.indexOf(':')
        val test = line
            .substring(startIndex + 1)
            .trim()
            .split(" ")
        val operation = test[0]
        val value = test[2].toLong()

        return when (operation) {
            "divisible" -> value
            else -> { throw IllegalStateException() }
        }
    }

    private fun parseOperation(line: String): (old: Long) -> Long {
        val startIndex = line.indexOf(':')
        val function = line
            .substring(startIndex + 1)
            .trim()
            .split("= ")[1]
        val components = function.split(" ")
        val operation = components[1]
        val operationValue = components[2]
        return { old ->
            val value = if (operationValue == "old") {
                old
            } else {
                operationValue.toLong()
            }
            when (operation) {
                "+" -> old + value
                "-" -> old - value
                "*" -> old * value
                "/" -> old / value
                else -> {
                    throw IllegalStateException()
                }
            }
        }
    }

    private fun parseItems(line: String): List<Long> {
        val startIndex = line.indexOf(':')
        val items = line.substring(startIndex + 1).trim()
        return items
            .split(", ")
            .map { it.toLong() }
    }

    fun getThrowTarget(i: Int, supermodulo: Long): Pair<Long, Int> {
        inspectedTimes++
        var worryLevel = items[i]
        worryLevel = if (inspectionAffectsWorryLevel) {
            operation(worryLevel) / 3
        } else {
            operation(worryLevel) % supermodulo
        }
        items[i] = worryLevel
        return if (items[i] % testNumber == 0L) {
            items[i] to outcomes[0]
        } else {
            items[i] to outcomes[1]
        }
    }
}
