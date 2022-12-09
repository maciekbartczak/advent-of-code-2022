fun main() {
    fun part1(input: List<String>): Int {
        val grid = makeGrid(input)
        return getVisibleTrees(grid)
    }

    fun part2(input: List<String>): Int {
        val grid = makeGrid(input)
        return grid
            .flatMapIndexed { y, row ->
                List(row.size) { x ->
                    grid.getScenicScore(x, y)
                }
            }
            .maxOf { it }
    }

    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}

private fun makeGrid(input: List<String>): List<List<Int>> {
    val grid = mutableListOf<List<Int>>()
    input
        .map { it.toIntArray() }
        .forEach { grid.add(it) }

    return grid
}

private fun getVisibleTrees(grid: List<List<Int>>): Int {
    val width = grid.first().size
    val height = grid.size
    var visibleTrees = (width + height) * 2 - 4
    println(visibleTrees)

    for (y in 1 until height - 1) {
        for (x in 1 until width - 1) {
            if (grid.isTreeVisible(x, y)) {
                println("tree at $x, $y is visible")
                visibleTrees++
            }
        }
    }

    return visibleTrees
}

private fun String.toIntArray(): List<Int> {
    return this
        .toCharArray()
        .map { it.digitToInt() }
}

private fun List<List<Int>>.isTreeVisible(x: Int, y: Int): Boolean {
    return this.checkRowVisibility(x, y) || this.checkColumnVisibility(x, y)
}

private fun List<List<Int>>.checkRowVisibility(x: Int, y: Int): Boolean {
    val row = this[y]
    val tree = this[y][x]
    var visible = true

    for (i in 0 until x) {
        if (row[i] >= tree) {
            visible = false
        }
    }
    if (visible) {
        return true
    }

    for (i in x + 1 until row.size) {
        if (row[i] >= tree) {
            return false
        }
    }
    return true
}

private fun List<List<Int>>.checkColumnVisibility(x: Int, y: Int): Boolean {
    val tree = this[y][x]
    var visible = true

    // check the upper side first
    for (i in 0 until y) {
        if (this[i][x] >= tree) {
            visible = false
        }
    }
    if (visible) {
        return true
    }

    for (i in y + 1 until this.size) {
        if (this[i][x] >= tree) {
            return false
        }
    }

    return true
}

private fun List<List<Int>>.getScenicScore(x: Int, y: Int): Int {
    val visibleTrees = mutableMapOf(
        'L' to 0,
        'R' to 0,
        'U' to 0,
        'D' to 0
    )
    val row = this[y]
    val tree = row[x]

    for (i in x - 1 downTo 0) {
        visibleTrees['L'] = visibleTrees['L']!! + 1
        if (row[i] >= tree) {
            break
        }
    }

    for (i in x + 1 until row.size) {
        visibleTrees['R'] = visibleTrees['R']!! + 1
        if (row[i] >= tree) {
            break
        }
    }

    for (i in y - 1 downTo 0) {
        visibleTrees['U'] = visibleTrees['U']!! + 1
        if (this[i][x] >= tree) {
            break
        }
    }

    for (i in y + 1 until this.size) {
        visibleTrees['D'] = visibleTrees['D']!! + 1
        if (this[i][x] >= tree) {
            break
        }
    }

    return visibleTrees
        .values
        .reduce { acc, i -> acc * i }
}