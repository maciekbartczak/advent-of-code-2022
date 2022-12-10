import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src/inputs", "$name.txt")
    .readLines()

fun readInputAsString(name: String) = File("src/inputs", "$name.txt")
    .readText()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

fun Pair<Int, Int>.offsetBy(offset: Pair<Int, Int>): Pair<Int, Int> {
    return this.copy(
        first = this.first + offset.first,
        second = this.second + offset.second
    )
}