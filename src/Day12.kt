import java.util.LinkedList
import java.util.Queue

fun main() {
    fun part1(input: List<String>): Int {
        val grid = makeGrid(input)
        val startingPosition = grid.findRootNode()
        val endPosition = bfs(grid, startingPosition)!!
        return endPosition.distance
    }

    fun part2(input: List<String>): Int {
        val grid = makeGrid(input)
        return grid.findRootNodes()
            .mapNotNull { bfs(grid, it) }
            .minOf { it.distance }
    }

    val testInput = readInput("Day12_test")
    check(part1(testInput) == 31)
    check(part2(testInput) == 29)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}

private class Node(
    val pos: Pair<Int, Int>,
    val distance: Int,
    val value: Char,
)
private fun makeGrid(input: List<String>): List<List<Char>> {
    return input.map { it.toList() }
}

private fun List<List<Char>>.findRootNode(): Pair<Int, Int> {
    for (y in this.indices) {
        for (x in this[y].indices) {
            if (this[y][x] == 'S') {
                return x to y
            }
        }
    }
    return -1 to -1
}

private fun List<List<Char>>.findRootNodes(): List<Pair<Int, Int>> {
    val positions = mutableListOf<Pair<Int, Int>>()
    for (y in this.indices) {
        for (x in this[y].indices) {
            val char = this[y][x]
            if (char.normalize() == 'a') {
                positions.add(x to y)
            }
        }
    }
    return positions
}

private fun bfs(grid: List<List<Char>>, startingPosition: Pair<Int, Int>): Node? {
    val startingNode = Node(
        startingPosition,
        0,
        grid[startingPosition.second][startingPosition.first]
    )
    val queue: Queue<Node> = LinkedList()
    val visited = mutableSetOf(startingPosition)

    queue.add(startingNode)

    while (queue.isNotEmpty()) {
        val currentNode = queue.remove()
        if (currentNode.value == 'E') {
            return currentNode
        }
        val neighbours = grid.getNeighbours(currentNode.pos)
        neighbours.forEach {
            if (it !in visited) {
                visited.add(it)
                queue.add(
                    Node(
                        it,
                        currentNode.distance + 1,
                        grid[it.second][it.first]
                    )
                )
            }
        }
    }
    return null
}

private fun List<List<Char>>.getNeighbours(pos: Pair<Int, Int>): List<Pair<Int, Int>> {
    val neighbours = mutableListOf<Pair<Int, Int>>()

    directions.forEach {
        val neighbour = pos.offsetBy(it)
        if (this.isPositionWithinBounds(neighbour) && this.getHeightDifference(neighbour, pos) <= 1) {
           neighbours.add(neighbour)
        }
    }

    return neighbours
}

private fun List<List<Char>>.isPositionWithinBounds(pos: Pair<Int, Int>): Boolean {
    val x = pos.first
    val y = pos.second

    return y in this.indices && x in this[0].indices
}

private fun List<List<Char>>.getHeightDifference(first: Pair<Int, Int>, second: Pair<Int, Int>): Int {
    val firstChar = this[first.second][first.first]
    val secondChar = this[second.second][second.first]

    return firstChar.normalize().code - secondChar.normalize().code
}

private fun Char.normalize(): Char {
    return when(this) {
        'S' -> 'a'
        'E' -> 'z'
        else -> this
    }
}

val directions = listOf(
    1 to 0,
    -1 to 0,
    0 to 1,
    0 to -1,
)