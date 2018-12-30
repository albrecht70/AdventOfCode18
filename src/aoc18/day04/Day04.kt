import java.io.File

object Day04 {

    @JvmStatic
    fun main(args: Array<String>) {
        part1()
        part2()
    }

    private fun part1() {
        val sleepTimes = getSleepTimes()

        val maxSleepGuard = sleepTimes.maxBy { m -> m.value.size }!!
        val maxSleepMinute = maxSleepGuard.value.groupBy { it }.maxBy { it.value.size }?.key!!
        println("Part1: ${maxSleepGuard.key} x $maxSleepMinute = ${maxSleepGuard.key * maxSleepMinute}")
    }

    private fun part2() {
        val sleepTimes = getSleepTimes()

        val guardMinutes = sleepTimes.flatMap { e ->
            e.value.map { minute ->
                e.key to minute
            }
        }
        val maxSleepGuardMinute = guardMinutes.groupBy { it }.maxBy { it.value.size }?.key!!
        println("Part2: ${maxSleepGuardMinute.first} x ${maxSleepGuardMinute.second} = ${maxSleepGuardMinute.first * maxSleepGuardMinute.second}")
    }

    private val guardPattern = " #([0-9]+) ".toRegex()
    private val timePattern = "^\\[.+:(\\d\\d)] ".toRegex()

    private fun getSleepTimes(): Map<Int, List<Int>> {
        val sleeps = mutableMapOf<Int, List<Int>>()
        var guard = 0
        var sleepStart = 0

        parseInput().sorted().forEach { line ->
            when {
                line.contains("Guard") -> guard = guardPattern.find(line)!!.groupValues[1].toInt()
                line.contains("asleep") -> sleepStart = timePattern.find(line)!!.groupValues[1].toInt()
                else -> {
                    val sleepEnd = timePattern.find(line)!!.groupValues[1].toInt()
                    val sleepTime = (sleepStart until sleepEnd).toList()
                    sleeps.merge(guard, sleepTime) { a, b -> a + b }
                }
            }
        }
        return sleeps
    }

    private fun parseInput(): List<String> =
        File(this.javaClass.getResource("aoc18/day04/input.txt").toURI()).readLines()
}
