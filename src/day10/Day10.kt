package day10

import readInput

fun main() {
    val input = readInput("day10", "input")

    val part1 = input
        .map { it.check() }
        .filterIsInstance<Result.Invalid>()
        .sumOf {
            it.actual.getErrorPoints()
        }

    println(part1)

    val points = input
        .map { it.check(autocomplete = true) }
        .filterIsInstance<Result.Completed>()
        .map { it.completion.fold(0L) { x, b -> (x * 5) + b.getCompletionPoints()  } }
        .sorted()

    println(points[points.lastIndex / 2])
}

private val validChars = setOf(
    ValidChar('(', ')', 3, 1),
    ValidChar('[', ']', 57, 2),
    ValidChar('{', '}', 1197, 3),
    ValidChar('<', '>', 25137, 4)
)

private fun Char.getClosingChar(): Char = validChars.find { it.opener == this }?.closer ?: error("not supported char: $this")
private fun Char.getOpeningChar(): Char = validChars.find { it.closer == this }?.opener ?: error("not supported char: $this")
private fun Char.isOpener(): Boolean = validChars.any { it.opener == this }
private fun Char.getErrorPoints(): Int = validChars.find { it.opener == this || it.closer == this }?.errorPoints ?: error("not supported char: $this")
private fun Char.getCompletionPoints(): Int = validChars.find { it.closer == this }?.completionPoints ?: error("not supported char: $this")

private fun String.check(autocomplete: Boolean = false): Result {
    val stack = mutableListOf<Char>()

    for (char in this) {
        if (char.isOpener()) {
            stack.add(char)
        } else {
            val valid = stack.last() == char.getOpeningChar()

            if (!valid) {
                return Result.Invalid(stack.last().getClosingChar(), char)
            }

            stack.removeAt(stack.lastIndex)
        }
    }

    if (autocomplete && stack.isNotEmpty()) {
        val completion = stack.reversed().map { it.getClosingChar() }.joinToString("")
        return Result.Completed(completion)
    }

    return Result.Valid
}

private sealed class Result {
    object Valid : Result()
    data class Invalid(val expected: Char, val actual: Char): Result()
    data class Completed(val completion: String): Result()
}

private data class ValidChar(val opener: Char, val closer: Char, val errorPoints: Int, val completionPoints: Int)