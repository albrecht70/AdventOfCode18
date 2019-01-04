import kotlin.math.abs

object Day25 {

    @JvmStatic
    fun main(args: Array<String>) {
        part1()
    }

    private val fixPoints = mutableListOf<Point>()

    private fun part1() {
        parseInput()

        val rest = mutableListOf<Point>()
        rest.addAll(fixPoints)

        val constellations = mutableListOf<MutableList<Point>>()
        val firstConst = mutableListOf(rest.removeAt(0))
        constellations.add(firstConst)

        while (rest.isNotEmpty()) {
            val next = rest.removeAt(0)
            val foundConsts = constellations.filter { cc -> cc.find { fp -> fp.nearby(next) } != null }

            if (foundConsts.isEmpty()) {
                val const = mutableListOf(next)
                constellations.add(const)
            } else {
                foundConsts.first().add(next)
                constellations.removeAll(foundConsts)
                constellations.add(foundConsts.flatten().toMutableList())
            }
        }
        println("Part1: constellations: #${constellations.size}")
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