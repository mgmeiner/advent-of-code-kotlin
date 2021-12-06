package day02

import readInput

fun main() {

    fun part1(commands: List<String>): Int {
        var position = 0
        var depth = 0

        for (command in commands) {
            val amount = command.getAmountOfCommand()

            if (command.contains("forward")) {
                position += amount
            } else if (command.contains("up")) {
                depth -= amount
            } else {
                depth += amount
            }
        }

        return position * depth
    }

    fun part2(commands: List<String>): Int {
        var aim = 0
        var position = 0
        var depth = 0

        for (command in commands) {
            val amount = command.getAmountOfCommand()

            if (command.contains("forward")) {
                position += amount
                depth += aim * amount
            } else if (command.contains("up")) {
                aim -= amount
            } else {
                aim += amount
            }
        }

        return position * depth
    }

    val testInput = readInput("day02", "test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("day02", "input")
    println(part1(input))
    println(part2(input))
}

private fun String.getAmountOfCommand() = "[0-9]+".toRegex().find(this)?.value?.toInt() ?: error("not able to get amount of command: $this")