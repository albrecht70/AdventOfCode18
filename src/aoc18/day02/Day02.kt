import java.io.File

object Day02 {

    @JvmStatic
    fun main(args: Array<String>) {
        part1()
        part2()
    }

    private fun part1() {
        val inputList = parseInput()
        val charCounts = inputList.map { str -> str.groupingBy { it }.eachCount() }
        val count2 = charCounts.count { c -> c.values.any { it == 2 } }
        val count3 = charCounts.count { c -> c.values.any { it == 3 } }
        println("Part1: $count2 x $count3 = ${count2 * count3}")
    }

    private fun part2() {
        val inputList = parseInput()
        val tries = mutableSetOf<String>()
        val len = inputList.first().length - 1

        for (idx in 0..len) {
            inputList.forEach { id ->
                val str = id.replaceRange(idx, idx+1, ".")
                if (!tries.add(str)) {
                    println("Part2: $str")
                    return
                }
            }
        }
    }

    private fun parseInput(): List<String> =
        File(this.javaClass.getResource("aoc18/day02/input.txt").toURI()).readLines()
}
