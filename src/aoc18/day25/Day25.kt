import kotlin.math.abs

object Day25 {

    @JvmStatic
    fun main(args: Array<String>) {
        teil1()
    }

    val fixPoints = mutableListOf<Point>()

    private fun teil1() {
        parseInput()

        val rest = mutableListOf<Point>()
        rest.addAll(fixPoints)

        val constellations = mutableListOf<MutableList<Point>>()
        val firstConst = mutableListOf(rest.removeAt(0))
        constellations.add(firstConst)

        while (rest.isNotEmpty()) {
            val ms1 = System.currentTimeMillis()
            val next = rest.removeAt(0)
            val foundConsts = mutableListOf<MutableList<Point>>()

            for (cc in constellations) {
                for (fp in cc) {
                    if (fp.nearby(next)) {
                        foundConsts.add(cc)
                        break
                    }
                }
            }

            when {
                foundConsts.isEmpty() -> {
                    val const = mutableListOf(next)
                    constellations.add(const)
                }
                foundConsts.size == 1 -> foundConsts.first().add(next)
                else -> {
                    println("merging...")
                    val const = mutableListOf(next)
                    for (fc in foundConsts) {
                        const.addAll(fc)
                        constellations.remove(fc)
                    }
                    constellations.add(const)
                }
            }
            val ms2 = System.currentTimeMillis()
            println("remain: #${rest.size} - constellations #${constellations.size} - timing: ${ms2 - ms1}")
        }
        println("constellations: #${constellations.size}")
    }

    private fun parseInput() {
        this.javaClass.getResourceAsStream("aoc18/day25/input.txt")
            .bufferedReader().forEachLine { line ->
                fixPoints.add(Point(line
                    .split(",")
                    .map { it.toInt() }
                    .toIntArray()))
            }
    }

    data class Point(val p: IntArray) {
        fun nearby(p2: Point): Boolean =
            abs(p[0] - p2.p[0]) + abs(p[1] - p2.p[1]) + abs(p[2] - p2.p[2]) + abs(p[3] - p2.p[3]) <= 3

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Point) return false

            if (!p.contentEquals(other.p)) return false

            return true
        }

        override fun hashCode(): Int {
            return p.contentHashCode()
        }
    }

}