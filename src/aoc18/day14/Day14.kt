object Day14 {

    @JvmStatic
    fun main(args: Array<String>) {
        part1()
        part2()
    }

    val numRecipes = 598701

    private fun part1() {
        val scoreboard = addRecipes { sb -> sb.size == numRecipes + 10 }

        val result = scoreboard.takeLast(10).joinToString("")
        println("Part1: $result")
    }

    private fun part2() {
        val inDigits = numRecipes.asDigits()
        val scoreboard = addRecipes { sb -> sb.endsWith(inDigits) }

        val result = scoreboard.size - inDigits.size
        println("Part2: $result")
    }

    private fun addRecipes(stopCondition: (List<Int>) -> Boolean): List<Int> {
        val scoreboard = mutableListOf(3, 7)
        var elf1 = 0
        var elf2 = 1
        var stop = false

        while (!stop) {
            val sum = scoreboard[elf1] + scoreboard[elf2]
            sum.asDigits().forEach { d ->
                if (!stop) {
                    scoreboard.add(d)
                    stop = stopCondition(scoreboard)
                }
            }
            elf1 = (elf1 + 1 + scoreboard[elf1]) % scoreboard.size
            elf2 = (elf2 + 1 + scoreboard[elf2]) % scoreboard.size
        }

        return scoreboard
    }

    private fun Int.asDigits(): List<Int> = this.toString().map { it.toString().toInt() }

    private fun List<Int>.endsWith(other: List<Int>): Boolean =
        if (this.size < other.size) false
        else this.slice(this.size - other.size until this.size) == other
}