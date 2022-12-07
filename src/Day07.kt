import java.lang.IllegalStateException

fun main() {
    fun part1(input: List<String>): Int {
        var path: String
        var rootDirectory: Node? = null
        var currentDirectory = Node(null, mutableListOf(), "/", 0)

        input.forEach {
            if (it.isCommand()) {
                val command = it.substring(2)
                if (command.startsWith("cd")) {
                    if (rootDirectory == null) {
                        rootDirectory = currentDirectory
                    } else {
                        if (currentDirectory.parent != null) {
                            currentDirectory.parent!!.size += currentDirectory.size
                        }
                        path = command.substring(3)
                        currentDirectory = if (path == "..") {
                            currentDirectory.parent!!
                        } else {
                            currentDirectory.findChildDirectory(path)
                        }
                    }
                }
            } else {
                val fileData = it.split(" ")
                var size = 0
                if (fileData[0] != "dir") {
                    size = fileData[0].toInt()
                }
                currentDirectory.children.add(
                    Node(currentDirectory, mutableListOf(), fileData[1], size)
                )
                currentDirectory.size += size
            }
        }

        prettyPrintStructure(rootDirectory!!, 0)
        val result = findDirectoriesWithSizeAtMost(rootDirectory!!, 100000)
        return result.sumOf { it.size }
    }

    fun part2(input: List<String>): Int {
       return input.size
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437)
//    check(part2(testInput) == 12)

    val input = readInput("Day07")
//    println(part1(input))
//    println(part2(input))
}
private class Node(
    var parent: Node?,
    var children: MutableList<Node>,
    var name: String,
    var size: Int
) {
    fun findChildDirectory(path: String): Node {
        return children.find {
            it.name == path
        } ?: throw IllegalStateException()
    }
}

private fun String.isCommand(): Boolean {
    return this.startsWith("$")
}

private fun prettyPrintStructure(node: Node, i: Int) {
    repeat(i) {
        print("  ")
    }
    print("- ${node.name} ")
    if (node.children.size > 0) {
        print("(dir,")
    } else {
        print("(file,")
    }
    print(" size=${node.size})\n")
    node.children.forEach {
        prettyPrintStructure(it, i + 1)
    }
}

private fun findDirectoriesWithSizeAtMost(node: Node, size: Int): List<Node> {
    val dirs = mutableListOf<Node>()

    if (node.children.isNotEmpty() && node.size <= size) {
        dirs.add(node)
    }
    node.children.forEach {
        dirs.addAll(findDirectoriesWithSizeAtMost(it, size))
    }
    return dirs
}
