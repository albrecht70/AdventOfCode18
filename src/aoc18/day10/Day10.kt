object Day10 {

    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        teil1u2()
    }

    @Throws(Exception::class)
    private fun teil1u2() {

        val points = ArrayList<Point>()
        this.javaClass.getResourceAsStream("aoc18/day10/input.txt")
            .bufferedReader().forEachLine {

                val idx1 = it.indexOf('<') + 1
                val idx2 = it.indexOf(',')
                val idx3 = it.indexOf('>')

                val idx4 = it.indexOf('<', idx3 + 1) + 1
                val idx5 = it.indexOf(',', idx3 + 1)
                val idx6 = it.indexOf('>', idx3 + 1)

                val xPos = it.substring(idx1, idx2).trim().toInt()
                val yPos = it.substring(idx2 + 1, idx3).trim().toInt()
                val xVel = it.substring(idx4, idx5).trim().toInt()
                val yVel = it.substring(idx5 + 1, idx6).trim().toInt()

                points.add(Point(xPos, yPos, xVel, yVel))
            }

        println("Points: $points")
        var lastArea = Long.MAX_VALUE
        var sec = 0;
        while (true) {
            // move and calc bounding box
            var xMin = Int.MAX_VALUE
            var xMax = Int.MIN_VALUE
            var yMin = Int.MAX_VALUE
            var yMax = Int.MIN_VALUE
            for (pp in points) {
                pp.xPos = pp.xPos + pp.xVel
                pp.yPos = pp.yPos + pp.yVel

                xMin = Math.min(xMin, pp.xPos)
                xMax = Math.max(xMax, pp.xPos)
                yMin = Math.min(yMin, pp.yPos)
                yMax = Math.max(yMax, pp.yPos)
            }
            val area = (xMax - xMin).toLong() * (yMax - yMin).toLong()
            println("bounding box area: $area: $xMin x $yMin -> $xMax x $yMax")
            if (lastArea < area) {
                break
            }
            lastArea = area
            sec++

            if (area < 1000) {
                for (col in yMin..yMax) {
                    for (row in xMin..xMax) {
                        val hasPoint = points.find { p -> (p.xPos == row && p.yPos == col) } != null
                        if (hasPoint)
                            print("#")
                        else
                            print(".")
                    }
                    println()
                }
            }
            println("Wait seconds: $sec")
        }
    }

    class Point(var xPos: Int, var yPos: Int, val xVel: Int, val yVel: Int) {
        override fun toString(): String {
            return "[$xPos,$yPos]->[$xVel,$yVel]"
        }
    }

}