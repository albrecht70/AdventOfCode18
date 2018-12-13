object Day12 {

    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        teil1()
        teil2()
    }

    @Throws(Exception::class)
    private fun teil2() {

        var plants = ""
        val rules = HashMap<String,Char>()
        this.javaClass.getResourceAsStream("aoc18/day12/input.txt")
            .bufferedReader().forEachLine {
                if (it.startsWith("initial state: ")) {
                    plants = it.substring("initial state: ".length)
                } else if (it.contains("=>")) {
                    val idx = it.indexOf(" => ")
                    val rule = it.substring(0, idx)
                    val apply = it.substring(idx + 4).toCharArray()[0]
                    rules[rule] = apply
                }
            }

        var idx0 = 0L
        var gen = 0
        while (gen < 50000000000) {
            gen++
            val lastPlants = plants
            plants = "....$plants...."

            val nextPlants = CharArray(plants.length, {i -> '.'})
            for (idx in 2..(plants.length-3)) {
                val substr = plants.substring(idx-2, idx+3)
                val entry = rules.entries.find { entry -> (entry.key.equals(substr)) }
                if (entry != null) {
                    nextPlants[idx] = entry.value
                }
            }

            val pos0 = nextPlants.indexOf('#')
            val pos1 = nextPlants.lastIndexOf('#')
            val nextStr = String(nextPlants).substring(pos0, pos1 + 1)
            idx0 += pos0 - 4
            //println("$gen: [$pos0, $pos1, $idx0] $nextStr")

            plants = nextStr
            if (nextStr == lastPlants) {
                break
            }
        }

        idx0 = 50000000000L - gen + idx0
        var sum = 0L
        for (ii in 0..(plants.length-1)) {
            if (plants[ii] == '#') {
                sum += (ii + idx0)
            }
        }
        println("Sum: $sum")
    }

    @Throws(Exception::class)
    private fun teil1() {
        var plants = ""
        val rules = HashMap<String,Char>()
        this.javaClass.getResourceAsStream("aoc18/day12/input.txt")
            .bufferedReader().forEachLine {
                if (it.startsWith("initial state: ")) {
                    plants = it.substring("initial state: ".length)
                } else if (it.contains("=>")) {
                    val idx = it.indexOf(" => ")
                    val rule = it.substring(0, idx)
                    val apply = it.substring(idx + 4).toCharArray()[0]
                    rules[rule] = apply
                }
            }

        var idx0 = 0
        var gen = 0L
        println("$gen: $plants")
        while (gen < 20) {
            gen++
            if (!plants.startsWith("....")) {
                println("Add.... [$gen] -> ${plants}")
                plants = "....$plants"
                idx0 += 4
            }
            if (!plants.endsWith("....")) {
                plants = "$plants...."
            }

            val nextPlants = CharArray(plants.length, {i -> '.'})
            for (idx in 2..(plants.length-3)) {
                val substr = plants.substring(idx-2, idx+3)
                val entry = rules.entries.find { entry -> (entry.key.equals(substr)) }
                if (entry != null) {
                    nextPlants[idx] = entry.value
                }
            }

            val nextStr = String(nextPlants)
            if (nextStr.equals(plants)) {
                println("Same plants: #$gen -> $plants")
                break
            }
            plants = nextStr
            println("$gen: $plants")
        }

        var sum = 0
        for (ii in 0..(plants.length-1)) {
            if (plants[ii] == '#') {
                sum += (ii - idx0)
            }
        }
        println("Sum: $sum")
    }

}