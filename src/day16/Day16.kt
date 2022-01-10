package day16

import readInput
import java.math.BigInteger

fun main() {
    val input = readInput("day16", "input").first()

    println(parsePacket(input).sumVersions())
    println(parsePacket(input).calculateValue())
}

private fun parsePacket(hex: String): Packet {
    val binary = hex.hexToBinary().iterator()

    fun doParse(): Packet {
        val version = binary.next(3).binaryToInt()
        val type = binary.next(3).binaryToInt()

        return if (type == 4) {
            var value = ""
            var i = 0
            while (true) {
                i++
                val next = binary.next(5)
                value += next.drop(1)
                if (next.startsWith('0')) break
            }

            Packet.Literal(version, type, value.binaryToLong(), 6 + (i * 5))
        } else {
            val lengthTypeId = binary.next(1)[0].digitToInt()

            val subPackets: List<Packet> = if (lengthTypeId == 0) {
                val expectedLength = binary.next(15).binaryToInt()

                buildList {
                    var length = 0
                    while (length < expectedLength) {
                        val next = doParse()
                        add(next)
                        length += next.length
                    }
                }
            } else {
                val expectedCountOfSubPackets = binary.next(11).binaryToInt()

                buildList {
                    var count = 0
                    while (count < expectedCountOfSubPackets) {
                        add(doParse())
                        count++
                    }
                }
            }

            val length = 7 + (if (lengthTypeId == 0) 15 else 11) + subPackets.sumOf { it.length }
            Packet.Operator(version, type, subPackets, length)
        }
    }

    return doParse()
}

private fun CharIterator.next(count: Int) = nextUntil { _, i -> i > count }

private fun CharIterator.nextUntil(stop: (c: Char, iteration: Int) -> Boolean): String {
    var result = ""

    if (!hasNext()) {
        return result
    }

    var iteration = 1
    do {
        result += nextChar()
        iteration++
    } while (hasNext() && !stop(result.last(), iteration))

    return result
}

private sealed class Packet(val version: Int, val type: Int, val length: Int) {
    class Literal(version: Int, type: Int, val value: Long, length: Int) : Packet(version, type, length)
    class Operator(version: Int, type: Int, val subPackets: List<Packet>, length: Int) : Packet(version, type, length)

    fun sumVersions(): Int = version + (if (this is Operator) subPackets.sumOf { it.sumVersions() } else 0)

    fun calculateValue(): Long {
        return when (this) {
            is Literal -> value
            is Operator -> {
                when (this.type) {
                    0 -> this.subPackets.sumOf { it.calculateValue() }
                    1 -> this.subPackets.fold(1) { v, p -> v * p.calculateValue() }
                    2 -> this.subPackets.minOf { it.calculateValue() }
                    3 -> this.subPackets.maxOf { it.calculateValue() }
                    5 -> if (this.subPackets[0].calculateValue() > this.subPackets[1].calculateValue()) 1 else 0
                    6 -> if (this.subPackets[0].calculateValue() < this.subPackets[1].calculateValue()) 1 else 0
                    7 -> if (this.subPackets[0].calculateValue() == this.subPackets[1].calculateValue()) 1 else 0
                    else -> error("")
                }
            }
        }
    }
}

private fun String.hexToBinary() = BigInteger("1$this", 16).toString(2).substring(1)
private fun String.binaryToInt() = toInt(2)
private fun String.binaryToLong() = toLong(2)