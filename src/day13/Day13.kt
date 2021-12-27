package day13

import readInput

fun main() {
    println(part1())
    part2()
}

private fun part1() = Folder(points).also { it.fold(commands.first()) }.points.size

private fun part2() = Folder(points).also {
    commands.forEach { cmd ->
        it.fold(cmd)
    }
    it.print()
}

private val input = readInput("day13", "input")

private val points = input
    .filter { it.contains(",") }
    .map {
        val (x, y) = it.split(",")
        Point(x.toInt(), y.toInt())
    }

private val commands = input
    .filter { it.startsWith("fold along") }
    .map {
        val (axis, valueRaw) = "([x,y])=(\\d+)".toRegex().find(it)!!.destructured
        Command(valueRaw.toInt(), axis[0])
    }

private class Folder(points: Collection<Point>) {
    var points: Set<Point> = points.toSet()
        private set

    fun fold(cmd: Command) {
        points = points.map {
            if (cmd.axis == 'y' && it.y > cmd.value) {
                it.copy(y = (cmd.value * 2) - it.y)
            } else if (cmd.axis == 'x' && it.x > cmd.value) {
                it.copy(x = (cmd.value * 2) - it.x)
            } else {
                it
            }
        }.toSet()
    }

    fun print() {
        for (y in 0..points.maxOf { it.y }) {
            for (x in 0..points.maxOf { it.x }) {
                points.find { it.x == x && it.y == y }?.run { print("#") } ?: print(" ")
            }
            println()
        }
    }
}

private data class Point(val x: Int, val y: Int)
private data class Command(val value: Int, val axis: Char)