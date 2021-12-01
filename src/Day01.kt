fun main() {
    fun countIncreases(numbers: List<Int>): Int {
        var increasedCount = 0
        var numberBefore = 0
        for (number in numbers) {
            if (numberBefore != 0 && number > numberBefore) {
                increasedCount++
            }

            numberBefore = number
        }

        return increasedCount
    }

    fun part1(input: List<String>): Int {
        return countIncreases(input.toNumbers())
    }

    fun part2(input: List<String>): Int {
        val numbers = input.toNumbers().windowed(3).map { it.sum() }
        return countIncreases(numbers)
    }

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("Day01_input")
    println(part1(input))
    println(part2(input))
}