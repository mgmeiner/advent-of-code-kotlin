package day15

import readInput
import java.util.*

fun main() {
    val points = parsePoints()

    println(findShortestPath(points))
    println(findShortestPath(points.increaseSizeBy5()))
}

private fun findShortestPath(points: List<Point>): Int {
    val start = points.first()
    val target = points.last()
    val visited = mutableSetOf<Point>()
    val open = PriorityQueue<Pair<Point, Int>>(Comparator.comparing { it.second })

    open += start to 0
    visited += start

    while (true) {
        val path = open.poll()

        val costToHere = path.second
        val last = path.first

        if (last == target) {
            return path.second
        }

        last.neighbours(points)
            .filter { it !in visited }
            .forEach {
                visited += it
                open += it to it.riskLevel + costToHere
            }
    }
}

private fun Point.neighbours(points: List<Point>): List<Point> {
    return buildList {
        points.findPoint(x + 1, y)?.run { add(this) }
        points.findPoint(x - 1, y)?.run { add(this) }
        points.findPoint(x, y + 1)?.run { add(this) }
        points.findPoint(x, y - 1)?.run { add(this) }
    }
}

private fun List<Point>.increaseSizeBy5(): List<Point> = buildList {
    val maxX = this.maxOf { it.x }
    val maxY = this.maxOf { it.y }

    for (point in this) {
        var ny = point.y
        var riskY = point.riskLevel
        for (y in 0 until 5) {
            var nx = point.x
            var riskX = riskY

            for (x in 0 until 5) {
                add(Point(nx, ny, riskX))

                nx += maxX + 1
                riskX++
                if (riskX > 9) {
                    riskX = 1
                }
            }

            riskY++
            if (riskY > 9) {
                riskY = 1
            }
            ny += maxY + 1
        }
    }
}

private fun List<Point>.findPoint(x: Int, y: Int): Point? = find { it.x == x && it.y == y }

private data class Point(val x: Int, val y: Int, val riskLevel: Int)

private fun parsePoints(): List<Point> = readInput("day15", "input")
    .flatMapIndexed { y: Int, s ->
        s.split("").filter { it.isNotEmpty() }.mapIndexed { x, it -> Point(x, y, it.toInt()) }
    }
