import java.util.*

object Day02 {

    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        teil1()
        teil2()
    }

    @Throws(Exception::class)
    private fun teil2() {

        val list = ArrayList<String>()
        val len = 26
        for (i in 0 until len) {
            //println("pos=$i")

            this.javaClass.getResourceAsStream("aoc18/day02/input.txt")
                .bufferedReader().forEachLine {
                val str = it.substring(0, i) + "." + it.substring(i + 1, it.length)
                if (list.contains(str)) {
                    println("GEFUNDEN: $str")
                } else {
                    list.add(str)
                }
            }
        }
    }

    @Throws(Exception::class)
    private fun teil1() {
        var mul2 = 0
        var mul3 = 0

        this.javaClass.getResourceAsStream("aoc18/day02/input.txt")
            .bufferedReader().forEachLine {
            val hasTwo = checkTwo(it)
            val hasThree = checkThree(it)
            println("$it ==> $hasTwo / $hasThree")

            if (hasTwo)
                mul2++
            if (hasThree)
                mul3++
        }

        println(mul2.toString() + " x " + mul3 + " = " + mul2 * mul3)
    }

    private fun checkTwo(line: String): Boolean {
        val map = mutableMapOf<Char, Int>()
        line.forEach {
            map[it] = map.getOrDefault(it, 0) + 1
        }

        return map.values.stream().anyMatch { it == 2 }
    }

    private fun checkThree(line: String): Boolean {
        val map = mutableMapOf<Char, Int>()
        line.forEach {
            map[it] = map.getOrDefault(it, 0) + 1
        }
        return map.values.stream().anyMatch { it == 3 }
    }
}
