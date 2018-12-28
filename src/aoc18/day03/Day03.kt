import kotlin.math.max

object Day03 {

    @JvmStatic
    fun main(args: Array<String>) {
        part1()
        part2()
    }

    private fun part1() {
        val claims = parseInput()
        val maxSize = claims.fold(0) { max, c -> max(max, (max(c.x + c.w, c.y + c.h))) }
        val fabric = Array(maxSize) { Array(maxSize) { 0 } }

        claims.forEach { c ->
            for (xPos in c.x until c.x + c.w) {
                for (yPos in c.y until c.y + c.h) {
                    fabric[xPos][yPos] += 1
                }
            }
        }

        val count = fabric.flatten().count { it > 1 }
        println("Part1: Square count: $count")
    }

    private fun part2() {
        val claims = parseInput()
        val maxSize = claims.fold(0) { max, c -> max(max, (max(c.x + c.w, c.y + c.h))) }

        val ids = mutableSetOf<String>()
        val fabric = Array(maxSize) { Array(maxSize) { mutableSetOf<String>() } }

        claims.forEach { c ->
            ids.add(c.id)
            for (xPos in c.x until c.x + c.w) {
                for (yPos in c.y until c.y + c.h) {
                    fabric[xPos][yPos].add(c.id)
                }
            }
        }

        ids.removeAll(fabric.flatten().filter { it.size > 1 }.flatten())
        println("Part2: ID: ${ids.first()}")
    }

    private val pattern = "^#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)$".toRegex()

    private fun parseInput(): List<Claim> {
        val claims = mutableListOf<Claim>()
        this.javaClass.getResourceAsStream("aoc18/day03/input.txt")
            .bufferedReader().forEachLine { line ->
                val m = pattern.find(line)?.destructured ?: throw IllegalArgumentException("invalid input: $line")
                claims.add(
                    Claim(
                        m.component1(), m.component2().toInt(), m.component3().toInt(),
                        m.component4().toInt(), m.component5().toInt()
                    )
                )

            }
        return claims
    }

    data class Claim(val id: String, val x: Int, val y: Int, val w: Int, val h: Int)
}
