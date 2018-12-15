object Day14 {

    @JvmStatic
    fun main(args: Array<String>) {
        teil1()
        teil2()
    }

    private fun teil2() {
        for (numRecipes in listOf(9, 5, 18, 2018, 598701)) {

            val inDigits = numRecipes.toString().toCharArray().map {it - '0'}

            val scoreboard = mutableListOf(3, 7)
            var elf1 = 0
            var elf2 = 1

            var result = -1
            var tick = 0
            do {
                val sum = scoreboard[elf1] + scoreboard[elf2]
                val digits = sum.toString().toCharArray().map { it - '0' }.toList()
                scoreboard.addAll(digits)

                elf1 = (elf1 + 1 + scoreboard[elf1]) % scoreboard.size
                elf2 = (elf2 + 1 + scoreboard[elf2]) % scoreboard.size

                if (tick % 100000 == 0) {
                    result = scoreboard.windowed(inDigits.size).indexOf(inDigits)
                }
                tick++
            } while (result < 0)

            println("Result[$numRecipes]: ticks: $tick -> $result")
        }
    }

    private fun teil1() {
        for (numRecipes in listOf(9, 5, 18, 2018, 598701)) {
            val scoreboard = mutableListOf(3, 7)
            var elf1 = 0
            var elf2 = 1

            do {
                val sum = scoreboard[elf1] + scoreboard[elf2]
                val digits = sum.toString().toCharArray().map { it - '0' }.toList()
                scoreboard.addAll(digits)

                elf1 = (elf1 + 1 + scoreboard[elf1]) % scoreboard.size
                elf2 = (elf2 + 1 + scoreboard[elf2]) % scoreboard.size

                //println("{$elf1, $elf2} : $scoreboard")
            } while (scoreboard.size <= numRecipes + 10)

            val result = scoreboard.subList(numRecipes, numRecipes + 10)
            println("Result[$numRecipes]: ${result.joinToString("")}")
        }
    }

}