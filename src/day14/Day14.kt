package day14

import readInput

private val input = readInput("day14", "input")

private val insertions = input.drop(2).map { Insertion.parse(it) }
private val initialTemplate = input.first().windowed(2).groupingBy { it }.eachCount().mapValues { it.value.toLong() }

fun main() {
    println(solve(10))
    println(solve(40))
}

private fun solve(size: Int) =
    generatePairs().take(size).last().countEachCharacter().values.sorted().let { it.last() - it.first() }

private fun generatePairs() = sequence {
    var next = initialTemplate

    while (true) {
        val before = next

        next = mutableMapOf()

        before.forEach { (pair, count) ->
            val insertion = insertions.find { it.pair == pair }

            if (insertion == null) {
                next[pair] = count
            } else {
                next.compute(insertion.pair[0] + insertion.insertion) { _, before -> (before ?: 0) + count }
                next.compute(insertion.insertion + insertion.pair[1]) { _, before -> (before ?: 0) + count }
            }
        }

        yield(next)
    }
}

private data class Insertion(val pair: String, val insertion: String) {
    companion object {
        fun parse(raw: String) = Insertion(raw.take(2), raw.last().toString())
    }
}

private fun Map<String, Long>.countEachCharacter(): Map<Char, Long> = map { it.key.first() to it.value }
    .groupBy({ it.first }, { it.second })
    .mapValues { it.value.sum() + if (it.key == input.first().last()) 1 else 0 }