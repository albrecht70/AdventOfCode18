object Day10 {

    @JvmStatic
    fun main(args: Array<String>) {
        part1and2()
    }

    private fun part1and2() {
        val points = parseInput()

        var lastArea = Long.MAX_VALUE
        var area = lastArea
        var sec = 0
        var xRange: IntRange
        var yRange: IntRange

        do {
            lastArea = area

            points.forEach {p ->
                p.xPos += p.xVel
                p.yPos += p.yVel
            }
            xRange = IntRange(points.minBy { it.xPos }!!.xPos, points.maxBy { it.xPos }!!.xPos)
            yRange = IntRange(points.minBy { it.yPos }!!.yPos, points.maxBy { it.yPos }!!.yPos)

            area = (xRange.endInclusive - xRange.start).toLong() * (yRange.endInclusive - yRange.start).toLong()
            sec++
        } while (area < lastArea)

        // undo last step
        points.forEach {p ->
            p.xPos -= p.xVel
            p.yPos -= p.yVel
        }
        xRange = IntRange(points.minBy { it.xPos }!!.xPos, points.maxBy { it.xPos }!!.xPos)
        yRange = IntRange(points.minBy { it.yPos }!!.yPos, points.maxBy { it.yPos }!!.yPos)
        sec--

        println("Part1: message:")
        yRange.forEach{ row ->
            xRange.forEach{ col ->
                if (points.any { p -> (p.xPos == col && p.yPos == row) })
                    print("#")
                else
                    print(".")
            }
            println()
        }
        println("Part2: wait seconds: $sec")

    }

    private fun parseInput(): List<Point> {
        val points = mutableListOf<Point>()
        this.javaClass.getResourceAsStream("aoc18/day10/input.txt")
            .bufferedReader().forEachLine { line ->
                line.split(",", "<", ">").map { it.trim() }.let {
                    points.add(Point(it[1].toInt(), it[2].toInt(), it[4].toInt(), it[5].toInt()))
                }
            }
        return points
    }

    data class Point(var xPos: Int, var yPos: Int, val xVel: Int, val yVel: Int)
}