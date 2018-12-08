object Day09 {

    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        teil1()
    }

    @Throws(Exception::class)
    private fun teil1() {

        this.javaClass.getResourceAsStream("aoc18/day09/test.txt")
            .bufferedReader().forEachLine {
            println("$it")
        }

    }

}