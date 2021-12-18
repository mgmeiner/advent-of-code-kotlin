fun main() {
    val testInput = readAsInts("day07", "test")

    check(solve(testInput, true) == 37)
    check(solve(testInput, false) == 168)

    val input = readAsInts("day07", "input")

    println(solve(input, true))
    println(solve(input, false))
}

fun solve(positions: List<Int>, constantRate: Boolean): Int {
    return positions.indices.map { horizontalPosition ->
        horizontalPosition to positions.sumOf { position ->
            val distance = (horizontalPosition.coerceAtLeast(position) - horizontalPosition.coerceAtMost(position))
            if (constantRate) {
                distance
            } else {
                (0..distance).sumOf { it }
            }
        }
    }.minByOrNull { it.second }!!.second
}