import java.io.File
import java.util.*

object Main {

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

            val file = File("day02-input.txt")
            file.forEachLine {
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

        val file = File("day02-input.txt")
        file.forEachLine {
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
