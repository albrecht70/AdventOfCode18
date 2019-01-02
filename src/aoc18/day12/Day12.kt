object Day12 {

    @JvmStatic
    fun main(args: Array<String>) {
        part1and2()
    }

    private var plants = ""
    private val rules = mutableMapOf<String, Char>()

    private fun part1and2() {
        parseInput()

        var offset0 = 0L
        var gen = 0
        while (gen < 50000000000) {
            gen++
            val lastPlants = plants
            plants = "....$plants...."

            val nextPlants = nextState()

            val pos0 = nextPlants.indexOf('#')
            val pos1 = nextPlants.lastIndexOf('#')
            val nextStr = String(nextPlants).substring(pos0, pos1 + 1)
            offset0 += pos0 - 4

            plants = nextStr
            if (nextStr == lastPlants) {
                break
            }
            if (gen == 20) {
                val sum = sumUp(offset0)
                println("Part1: sum: $sum")
            }
        }

        val sum = sumUp(offset0 + 50000000000L - gen)
        println("Part2: sum: $sum")
    }

    private fun sumUp(offset0: Long) = plants.mapIndexed { idx, c -> if (c == '#') (idx + offset0) else 0 }.sum()

    private fun nextState(): CharArray {
        val nextPlants = CharArray(plants.length) { '.' }
        for (idx in 2..(plants.length - 3)) {
            val substr = plants.substring(idx - 2, idx + 3)
            val entry = rules.entries.find { entry -> (entry.key == substr) }
            if (entry != null) {
                nextPlants[idx] = entry.value
            }
        }
        return nextPlants
    }

    private fun parseInput() {
        rules.clear()
        this.javaClass.getResourceAsStream("aoc18/day12/input.txt")
            .bufferedReader().forEachLine { line ->
                if (line.startsWith("initial state: ")) {
                    plants = line.substring("initial state: ".length)
                } else if (line.contains("=>")) {
                    val idx = line.indexOf(" => ")
                    val rule = line.substring(0, idx)
                    val apply = line.substring(idx + 4).toCharArray()[0]
                    rules[rule] = apply
                }
            }
    }

}