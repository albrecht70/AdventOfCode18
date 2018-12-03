import java.io.File
import java.util.*

object Main {

    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        var sum = 0
        var pos = 0
        val sumList = ArrayList<Int>()

        var found = false;
        while (!found) {

            val file = File("day01-input.txt")
            file.forEachLine {
                sum += Integer.parseInt(it)
                pos++
                //println("pos/sum: $pos / $sum")

                if (sumList.contains(sum)) {
                    println("==> Sum: " + sum)
                    found = true
                    System.exit(1)
                }
                sumList.add(sum)
            }
        }
    }
}
