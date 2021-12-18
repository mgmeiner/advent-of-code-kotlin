package day06

import readAsInts

fun main() {
    val testInput = readAsInts("day06", "test")

    check(solve(testInput, 18) == 26L)
    check(solve(testInput, 80) == 5934L)

    val input = readAsInts("day06", "input")

    println(solve(input, 80))
    println(solve(input, 256))
}

fun solve(initialState: List<Int>, days: Int): Long {
    val fishPerDay = LongArray(9).apply {
        initialState.forEach { this[it]++ }
    }

    for (day in 1..days) {
        val day0Fish = fishPerDay[0]

        fishPerDay.indices.forEach {
            if (it == 8) {
                fishPerDay[it] = day0Fish
            } else {
                fishPerDay[it] = fishPerDay[it + 1]
            }
        }

        fishPerDay[6] += day0Fish
    }

    return fishPerDay.sum()
}