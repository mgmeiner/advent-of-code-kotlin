package day05

import readInput
import kotlin.math.abs
import kotlin.math.sign

private val numberRegex = "[0-9]+".toRegex()

fun main() {
    val testInput = readInput("day05", "test")

    check(calculate(testInput, false) == 5)
    check(calculate(testInput, true) == 12)

    val input = readInput("day05", "input")
    println(calculate(input, false))
    println(calculate(input, true))
}

private fun calculate(input: List<String>, diagonal: Boolean): Int {
    val matrix: List<MutableList<Int>> = List(1000) { List(1000) { 0 }.toMutableList() }

    input.forEach { line ->
        val (x1, y1, x2, y2) = numberRegex.findAll(line).map { it.value.toInt() }.toList()

        if (y1 == y2) {
            for (i in x1.coerceAtMost(x2)..x2.coerceAtLeast(x1)) {
                matrix[y1][i]++
            }
        } else if (x1 == x2) {
            for (i in y1.coerceAtMost(y2)..y2.coerceAtLeast(y1)) {
                matrix[i][x1]++
            }
        } else if (diagonal) {
            val xD = (x2 - x1).sign
            val yD = (y2 - y1).sign

            val until = maxOf(abs(x1 - x2), abs(y1 - y2))

            var bx = x1
            var by = y1

            matrix[y1][x1]++

            for (i in 1..until) {
                bx += xD
                by += yD

                matrix[by][bx]++
            }
        }
    }

    return matrix.flatten().count { it > 1 }
}