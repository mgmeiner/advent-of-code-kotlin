package day04

import readInput

fun main() {
    val testInput = readInput("day04", "test")

    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("day04", "input")

    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Int {
    val numbers = input.first().split(",").map { it.toInt() }

    val game = Game(input.drop(2))

    val winner = numbers
        .asSequence()
        .onEach { game.drawNumber(it) }
        .mapNotNull { game.winner }
        .firstOrNull() ?: error("no winner")

    return winner.getSumOfUnmarkedNumbers(game.drawnNumbers) * game.lastDrawnNumber
}

private fun part2(input: List<String>): Int {
    val numbers = input.first().split(",").map { it.toInt() }

    val game = Game(input.drop(2))

    val lastOne = numbers
        .asSequence()
        .onEach { game.drawNumber(it) }
        .mapNotNull { game.lastOne }
        .firstOrNull() ?: error("no winner")

    return lastOne.getSumOfUnmarkedNumbers(game.drawnNumbers) * game.lastDrawnNumber
}

class Game(private val boardsRaw: List<String>) {

    private val boards: List<Board> = createBoards()

    val drawnNumbers = mutableListOf<Int>()
    private val winners = mutableSetOf<Board>()

    private fun createBoards() = boardsRaw.asSequence()
        .filter { it.isNotEmpty() }
        .windowed(5, 5)
        .map { it.map { row -> row.split(" ").mapNotNull { n -> n.toIntOrNull() } } }
        .map { Board(it) }.toList()

    fun drawNumber(number: Int) {
        drawnNumbers.add(number)
        winners += boards.filter { it.hasWon(drawnNumbers) }
    }

    val winner: Board?
        get() = winners.firstOrNull()

    val lastOne: Board?
        get() {
            return if (boards.size != winners.size) {
                null
            } else {
                winners.last()
            }
        }

    val lastDrawnNumber: Int
        get() = drawnNumbers.last()
}

data class Board(private val fields: List<List<Int>>) {

    fun hasWon(draws: List<Int>): Boolean {
        return fields.any { row -> row.all { it in draws } } || fields.indices.any { i ->
            fields.map { it.elementAt(i) }.all { it in draws }
        }
    }

    fun getSumOfUnmarkedNumbers(draws: List<Int>): Int {
        return fields.flatten().filter { it !in draws }.sum()
    }
}

