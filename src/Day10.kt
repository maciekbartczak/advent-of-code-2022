fun main() {
    fun part1(input: List<String>): Int {
        val cpu = Cpu()
        cpu.executeInstructions(input)

        return cpu.signalStrengthMap.values.sum()
    }

    fun part2(input: List<String>): String {
        val cpu = Cpu()
        cpu.executeInstructions(input)

        return cpu.getSprite()
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 13140)
    println(part2(testInput))

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}

private enum class OPERATION {
    NOOP,
    ADDX
}

private class Cpu {
    private var registerX = 1
    private var cycle = 1
    private var cycleToConsider = 20
    private var drawPosition = 0 to 0
    val signalStrengthMap = mutableMapOf<Int, Int>()
    private var sprite = StringBuilder()

    fun executeInstructions(instructions: List<String>) {
        instructions.forEach {
            val (op, value) = it.parseInput()
            executeCycle(op, value)
        }
    }

    fun getSprite(): String {
        return sprite.toString()
    }
    private fun executeCycle(operation: OPERATION, value: Int) {
        if (cycle == cycleToConsider) {
            calculateSignalStrength()
            cycleToConsider += 40
        }
        drawSprite()
        cycle++
        if (operation == OPERATION.ADDX) {
            executeCycle(OPERATION.NOOP, 0)
            registerX += value
        }
    }

    private fun calculateSignalStrength() {
        signalStrengthMap[cycle] = cycle * registerX
    }

    private fun drawSprite() {
        val spriteX = registerX - 1 .. registerX + 1
        if (drawPosition.first in spriteX) {
            sprite.append('#')
        } else {
            sprite.append('.')
        }


        drawPosition = if (drawPosition.first == 39) {
            sprite.append('\n')
            drawPosition.copy(
                first = 0,
                second = (drawPosition.second + 1) % 5
            )
        }
        else {
            drawPosition.offsetBy(1 to 0)
        }
    }
}
private fun String.parseInput(): Pair<OPERATION, Int> {
    if (this == "noop") {
        return OPERATION.NOOP to 0
    }
    val split = this.split(" ")
    return OPERATION.valueOf(split[0].uppercase()) to split[1].toInt()
}






