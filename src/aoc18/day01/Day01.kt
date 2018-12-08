import java.util.*

object Day01 {

    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        var sum = 0
        var pos = 0
        val sumList = ArrayList<Int>()

        var found = false;
        while (!found) {

            this.javaClass.getResourceAsStream("aoc18/day01/input.txt")
                .bufferedReader().forEachLine {

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
