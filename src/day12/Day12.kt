package day12

import readInput

fun main() {
    val caves = parseCaves(readInput("day12", "input"))

    val start = caves.find { it.isStart }!!

    val part1 = findPaths(listOf(start)) { currentPath, cave -> cave in currentPath }
        .count { it.any { cave -> cave.isSmallCave } }

    println(part1)

    val part2 = findPaths(listOf(start)) { currentPath, cave ->
        cave in currentPath && currentPath.filter { it.isSmallCave }.groupBy { it }.any { it.value.size == 2 }
    }.count()

    println(part2)
}

private fun findPaths(
    currentPath: List<Cave>,
    smallCaveCheck: (currentPath: List<Cave>, cave: Cave) -> Boolean
): List<List<Cave>> {
    val cave = currentPath.last()

    if (cave.isEnd) {
        return listOf(currentPath)
    }

    return cave.connections
        .filter { !it.isStart && !(it.isSmallCave && smallCaveCheck(currentPath, it)) }
        .flatMap { it -> findPaths(currentPath + it, smallCaveCheck) }
}

private fun parseCaves(input: List<String>): Set<Cave> {
    val caves = mutableSetOf<Cave>()

    input.forEach { raw ->
        val (from, to) = raw.split("-")

        val fromPoint = caves.find { it.id == from } ?: Cave(from).also { caves.add(it) }
        val toPoint = caves.find { it.id == to } ?: Cave(to).also { caves.add(it) }

        fromPoint.connections.add(toPoint)
        toPoint.connections.add(fromPoint)
    }

    return caves
}

private data class Cave(val id: String) {
    val connections = mutableSetOf<Cave>()

    val isStart = id == "start"
    val isEnd = id == "end"
    val isSmallCave = !isStart && !isEnd && Character.isLowerCase(id[0])
}