import java.io.File

object Day05 {

    @JvmStatic
    fun main(args: Array<String>) {
        part1()
        part2()
    }

    private fun part1() {
        val polymer = parseInput()
        val reduced = react(polymer)
        println("Part1: Unit count: ${reduced.size}")
    }

    private fun part2() {
        val polymer = parseInput()
        val shortest = ('a'..'z').map { c -> react(polymer, c).size }.min()
        println("Part2: Shortest polymer: $shortest")
    }

    private fun react(polymer: String, ignore: Char? = null): List<Char> =
        polymer.fold(mutableListOf<Char>()) { reduced, char ->
            when {
                ignore != null && char.equals(ignore, true) -> Unit
                canReact(reduced.lastOrNull(), char) -> reduced.removeAt(reduced.size - 1)
                else -> reduced.add(char)
            }
            reduced
        }

    private var charDiff = 'a'.toInt() - 'A'.toInt()

    private fun canReact(ch1: Char?, ch2: Char?): Boolean {
        if (ch1 == null || ch2 == null) {
            return false
        }
        return Math.abs(ch1 - ch2) == charDiff
    }

    private fun parseInput(): String =
        File(this.javaClass.getResource("aoc18/day05/input.txt").toURI()).readText()
}