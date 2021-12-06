package day03

import readInput

fun main() {
    fun part1(input: List<String>): Int {
        var gammaRate = ""
        var epsilonRate = ""

        for (i in input.first().indices) {
            if (input.getMostCommonCharacterAtPosition(i) == "1") {
                gammaRate += "1"
                epsilonRate += "0"
            } else {
                gammaRate += "0"
                epsilonRate += "1"
            }
        }

        return gammaRate.toInt(2) * epsilonRate.toInt(2)
    }

    fun part2(input: List<String>) = calcLifeSupportRating(input, true) * calcLifeSupportRating(input, false)

    val testInput = readInput("day03", "test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("day03", "input")
    println(part1(input))
    println(part2(input))
}

fun calcLifeSupportRating(input: List<String>, keepMostCommon: Boolean): Int {
    var filteredInput = input

    for (i in filteredInput.first().indices) {

        val c = if (keepMostCommon) {
            filteredInput.getMostCommonCharacterAtPosition(i)
        } else {
            filteredInput.getFewestCharacterAtPosition(i)
        }

        filteredInput = filteredInput.filter { it[i].toString() == c }

        if (filteredInput.size == 1) {
            return filteredInput.first().toInt(2)
        }
    }

    error("should not happen")
}

private fun List<String>.getMostCommonCharacterAtPosition(position: Int): String {
    return if (this.map { it[position] }.count { it == '1' } >= this.map { it[position] }.count { it == '0' }) {
        "1"
    } else {
        "0"
    }
}

private fun List<String>.getFewestCharacterAtPosition(position: Int): String {
    return if (getMostCommonCharacterAtPosition(position) == "1") {
        "0"
    } else {
        "1"
    }
}

