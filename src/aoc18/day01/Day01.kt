object Day01 {

    @JvmStatic
    fun main(args: Array<String>) {

        val freqList = parseInput()
        println("Part1: sum=${freqList.sum()}")

        val seenFreqs = mutableSetOf<Int>()
        var sum = 0
        val freq = freqList.toInfiniteSequence()
            .map { f ->
                sum += f
                sum
            }.first { !seenFreqs.add(it) }

        println("Part2: freq=$freq")
    }

    private fun parseInput(): List<Int> {
        val freqList = mutableListOf<Int>()
        this.javaClass.getResourceAsStream("aoc18/day01/input.txt")
            .bufferedReader().forEachLine { line ->
                freqList.add(line.toInt())
            }
        return freqList
    }

    private fun <T> List<T>.toInfiniteSequence(): Sequence<T> = sequence {
        while (true) {
            yieldAll(this@toInfiniteSequence)
        }
    }

}