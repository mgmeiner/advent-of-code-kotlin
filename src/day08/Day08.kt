package day08

import readInput

fun main() {
    val testInput = readInput("day08", "test")

    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readInput("day08", "input")

    println(part1(input))
    println(part2(input))
}

private fun part1(lines: List<String>) = lines
    .map { it.split("|")[1] }
    .flatMap { it.split(" ") }
    .filter { it.isNotEmpty() }
    .map { it.count() }
    .count { it == 2 || it == 4 || it == 3 || it == 7 }

private fun part2(lines: List<String>) = lines.sumOf { line ->
    val trainingSet = line.split("|")[0].split(" ").filter { it.isNotEmpty() }
    val output = line.split("|")[1].split(" ").filter { it.isNotEmpty() }

    val mapper = Mapper(trainingSet)

    output.joinToString("") { no ->
        mapper.mapToNumber(no).toString()
    }.toInt()
}

private class Mapper(val trainingSet: List<String>) {

    private val mapping: Map<Set<Char>, Int> = createMapping()

    private fun createMapping(): Map<Set<Char>, Int> {
        val parts = trainingSet.map { it.toSet() }

        val mapping = mutableMapOf<Int, Set<Char>>()

        mapping[1] = parts.find { it.count() == 2 }!!
        mapping[7] = parts.find { it.count() == 3 }!!
        mapping[4] = parts.find { it.count() == 4 }!!
        mapping[8] = parts.find { it.count() == 7 }!!

        mapping[9] = parts.find { it.count() == 6 && it.containsAll(mapping.getValue(4)) }!!
        mapping[0] =
            parts.find { it.count() == 6 && it.containsAll(mapping.getValue(7)) && it != mapping.getValue(9) }!!
        mapping[6] = parts.find { it.count() == 6 && it != mapping.getValue(0) && it != mapping.getValue(9) }!!

        mapping[3] = parts.find { it.count() == 5 && it.containsAll(mapping.getValue(7)) }!!
        mapping[5] =
            parts.find { it.count() == 5 && it != mapping.getValue(3) && it.intersect(mapping.getValue(4)).size == 3 }!!
        mapping[2] = parts.find { it.count() == 5 && it != mapping.getValue(3) && it != mapping.getValue(5) }!!

        return mapping.toList().associate { it.second to it.first }
    }

    fun mapToNumber(part: String) = mapping.getValue(part.toSet())
}

