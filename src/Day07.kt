import java.lang.IllegalStateException
import java.util.function.Predicate

fun main() {

    fun part1(input: List<String>): Int {
        val rootDirectory = createDirectoryTree(input)
        val result = findDirectoriesWithSizeAtMost(rootDirectory, 100000)
        return result.sumOf { it.size }
    }

    fun part2(input: List<String>): Int {
        val rootDirectory = createDirectoryTree(input)

        val unusedSpace = 70000000 - rootDirectory.size
        val requiredSpace = 30000000 - unusedSpace
        val candidates = findDirectoriesWithSizeAtLeast(rootDirectory, requiredSpace)

        return candidates.minOf { it.size }
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437)
    check(part2(testInput) == 24933642)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}

private class Directory(
    var parent: Directory?,
    var childDirectories: MutableList<Directory>,
    var name: String,
    var size: Int
) {
    fun findChildDirectory(path: String): Directory {
        return childDirectories.find {
            it.name == path
        } ?: throw IllegalStateException()
    }
}

private fun createDirectoryTree(input: List<String>): Directory {
    var currentDirectory = Directory(null, mutableListOf(), "/", 0)
    val rootDirectory = currentDirectory

    input.forEachIndexed { index, it ->
        if (it.isCommand()) {
            val command = it.substring(2)
            if (command.startsWith("cd") && index != 0) {
                val path = command.substring(3)
                currentDirectory = if (path == "..") {
                    currentDirectory.parent!!
                } else {
                    currentDirectory.findChildDirectory(path)
                }
            }
        } else {
            val entryData = it.split(" ")
            if (entryData[0] == "dir") {
                currentDirectory.childDirectories.add(
                    Directory(currentDirectory, mutableListOf(), entryData[1], 0)
                )
            } else {
                currentDirectory.size += entryData[0].toInt()
            }
        }
    }

    calculateDirectorySizes(rootDirectory)
    prettyPrintStructure(rootDirectory, 0)

    return rootDirectory
}
private fun String.isCommand(): Boolean {
    return this.startsWith("$")
}

private fun prettyPrintStructure(directory: Directory, i: Int) {
    repeat(i) {
        print("  ")
    }
    print("- ${directory.name} ")
    print(" size=${directory.size}\n")
    directory.childDirectories.forEach {
        prettyPrintStructure(it, i + 1)
    }
}

private fun calculateDirectorySizes(directory: Directory) {
    directory.childDirectories.forEach {
        calculateDirectorySizes(it)
        directory.size += it.size
    }
}

private fun findDirectoriesWithSizeAtMost(directory: Directory, size: Int): List<Directory> {
    return findDirectoriesWithSize(
        directory,
        size
    ) { it.size <= size }
}

private fun findDirectoriesWithSizeAtLeast(directory: Directory, size: Int): List<Directory> {
    return findDirectoriesWithSize(
        directory,
        size
    ) { it.size >= size }
}

private fun findDirectoriesWithSize(directory: Directory, size: Int, predicate: Predicate<Directory>): List<Directory> {
    val dirs = mutableListOf<Directory>()

    if (predicate.test(directory)) {
        dirs.add(directory)
    }
    directory.childDirectories.forEach {
        dirs.addAll(findDirectoriesWithSize(it, size, predicate))
    }
    return dirs
}