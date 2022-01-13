package day17

import readInputAsString
import kotlin.math.abs

fun main() {
    val input = readInputAsString("day17", "input")

    val ta = TargetArea.parse(input)

    println(part1(ta))
    println(part2(ta))
}

private fun part1(ta: TargetArea): Int {
    return ta
        .possibleVelocities()
        .mapNotNull { velocity ->
            val points = velocity.move().takeWhile { point -> !point.over(ta) || point.within(ta) }.toList()
            if (!points.last().within(ta)) null else points
        }
        .map { points -> points.maxOf { it.y } }
        .maxOf { it }
}

private fun part2(ta: TargetArea): Int {
    return ta.possibleVelocities().count { velocity ->
        velocity.move().takeWhile { point -> !point.over(ta) || point.within(ta) }.toList().any { point -> point.within(ta) }
    }
}

private fun Velocity.move(): Sequence<Point> = sequence {
    var point = Point(0, 0)
    var velocity = this@move

    while (true) {
        point = Point(point.x + velocity.x, point.y + velocity.y)
        velocity = Velocity(velocity.x.let {
            if (it == 0) {
                0
            } else if (it > 0) {
                it - 1
            } else {
                it + 1
            }
        }, velocity.y - 1)

        yield(point)
    }
}

private data class TargetArea(val xs: Int, val xe: Int, val ys: Int, val ye: Int) {
    companion object {
        fun parse(input: String): TargetArea {
            val (xs, xe, ys, ye) = "-?[0-9]+".toRegex().findAll(input).map { it.value.toInt() }.toList()
            return TargetArea(xs, xe, ys, ye)
        }
    }
}

private data class Point(val x: Int, val y: Int)

private fun Point.within(ta: TargetArea): Boolean = x in (ta.xs..ta.xe) && y in (ta.ys..ta.ye)
private fun Point.over(ta: TargetArea): Boolean = y < minOf(ta.ys, ta.ye) || x > maxOf(ta.xs, ta.xe)

private typealias Velocity = Point

private fun TargetArea.possibleVelocities(): List<Velocity> = buildList {
    for (x in 0 .. xe) {
        for (y in ys..abs(ys)) {
            add(Velocity(x, y))
        }
    }
}
