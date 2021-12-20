package day09

import readInput

typealias Matrix = List<List<Point>>

fun main() {
    val testInput = readInput("day09", "test")

    check(part1(testInput.toMatrix()) == 15)
    check(part2(testInput.toMatrix()) == 1134)

    val input = readInput("day09", "input")

    println(part1(input.toMatrix()))
    println(part2(input.toMatrix()))
}

fun part1(matrix: Matrix): Int {
    return matrix.flatten()
        .filter { it.isLowerThanAllSurrounding(matrix) }
        .sumOf { it.value + 1 }
}

fun part2(matrix: Matrix): Int {
    return matrix
        .asSequence()
        .flatten()
        .filter { it.isLowerThanAllSurrounding(matrix) }
        .map { it.calculateBasinSize(matrix) }
        .sortedByDescending { it }
        .take(3)
        .reduce { a, b -> a * b }
}

fun Point.isLowerThanAllSurrounding(matrix: Matrix): Boolean {
    return this.value < (matrix.getValueOrDefault(row + -1, column)) &&
        this.value < (matrix.getValueOrDefault(row + 1, column)) &&
        this.value < (matrix.getValueOrDefault(row, column - 1)) &&
        this.value < (matrix.getValueOrDefault(row, column + 1))
}

fun Point.calculateBasinSize(
    matrix: Matrix,
    basinPoints: MutableSet<Point> = mutableSetOf()
): Int {
    if (this.value == 9 || basinPoints.contains(this)) {
        return 0
    }

    basinPoints.add(this)

    matrix.getPoint(row - 1, column)?.run { this.calculateBasinSize(matrix, basinPoints) }
    matrix.getPoint(row + 1, column)?.run { this.calculateBasinSize(matrix, basinPoints) }
    matrix.getPoint(row, column - 1)?.run { this.calculateBasinSize(matrix, basinPoints) }
    matrix.getPoint(row, column + 1)?.run { this.calculateBasinSize(matrix, basinPoints) }

    return basinPoints.size
}

data class Point(val row: Int, val column: Int, val value: Int)

fun List<String>.toMatrix(): Matrix = mapIndexed { i, line ->
    line.toList().mapIndexed { index, it -> Point(i, index, it.toString().toInt()) }
}

fun Matrix.getValueOrDefault(row: Int, column: Int) = getPoint(row, column)?.value ?: Int.MAX_VALUE

fun Matrix.getPoint(row: Int, column: Int) = getOrNull(row)?.getOrNull(column)