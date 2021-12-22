package day11

import readInput

typealias FlashedInStep = MutableSet<Pair<Int, Int>>
typealias Octopuses = List<MutableList<Int>>

fun main() {
    val input = readInput("day11", "input");

    val simulator = OctopusSimulator(input)

    repeat(100) { simulator.nextStep() }

    // part 1
    println(simulator.flashed)

    while (simulator.nextStep() != 100) {
    }

    // part 2
    println(simulator.currentStep)
}

private class OctopusSimulator(raw: List<String>) {
    private val octopuses: Octopuses = raw.map { line -> line.chunked(1).map { it.toInt() }.toMutableList() }

    var currentStep = 0
        private set

    var flashed = 0
        private set

    fun nextStep(): Int {
        currentStep++

        octopuses.forEach { row, col -> octopuses[row][col]++ }

        val flashedInStep = mutableSetOf<Pair<Int, Int>>()

        octopuses.forEach { row, col ->
            if (octopuses[row][col] > 9) {
                flash(row, col, flashedInStep)
            }
        }

        flashed += flashedInStep.size

        return flashedInStep.size
    }

    private fun flash(r: Int, c: Int, flashedInStep: FlashedInStep) {
        if (octopuses.getOrNull(r)?.getOrNull(c) == null || flashedInStep.contains(Pair(r, c))) {
            return
        }

        octopuses[r][c]++

        if (octopuses[r][c] > 9) {
            octopuses[r][c] = 0
            flashedInStep.add(Pair(r, c))

            flash(r - 1, c - 1, flashedInStep)
            flash(r - 1, c, flashedInStep)
            flash(r - 1, c + 1, flashedInStep)

            flash(r, c - 1, flashedInStep)
            flash(r, c + 1, flashedInStep)

            flash(r + 1, c - 1, flashedInStep)
            flash(r + 1, c, flashedInStep)
            flash(r + 1, c + 1, flashedInStep)
        }
    }

    private fun Octopuses.forEach(block: (row: Int, col: Int) -> Unit) {
        for ((i, row) in this.withIndex()) {
            for (col in row.indices) {
                block(i, col)
            }
        }
    }
}

