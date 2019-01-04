object Day17 {

    @JvmStatic
    fun main(args: Array<String>) {
        part1and2()
    }

    private const val dim = 2000

    private val ground = Array(dim) { Array(dim) {'.'} }
    private val spring = Pos(500, 0)

    private var yMax = Int.MIN_VALUE
    private var tick = 0

    private fun part1and2() {
        ground[spring.y][spring.x] = '+'
        parseInput()

        var flows = mutableListOf(spring)
        do {
            val nextFlows = mutableListOf<Pos>()
            flows.forEach {fp ->
                nextFlows.addAll(nextFlow(fp))
            }
            flows = nextFlows

            //println("-> $flows, tick: $tick")
            tick++
        } while (flows.isNotEmpty())

        val count = ground.flatten().count { ch -> ch == '~' || ch == '|'}
        val count2 = ground.flatten().count { ch -> ch == '~'}
        println("Part1: water & flow count: $count")
        println("Part2: water only count: $count2")
    }

    private fun nextFlow(flowPos: Pos): List<Pos> {
        // down
        val nextFlow = Pos(flowPos.x, flowPos.y + 1)
        if (nextFlow.y > yMax) {
            return emptyList()
        }
        if (ground[nextFlow.y][nextFlow.x] == '.') {
            ground[nextFlow.y][nextFlow.x] = '|'
            return listOf(nextFlow)
        }

        // left & right
        val nextFlows = mutableListOf<Pos>()
        for (incrx in listOf(-1, 1)) {
            val nextFlow = Pos(flowPos.x + incrx, flowPos.y)
            if (ground[nextFlow.y][nextFlow.x] == '.') {
                ground[nextFlow.y][nextFlow.x] = '|'
                nextFlows.add(nextFlow)
            }
        }
        if (nextFlows.size > 0) {
            return nextFlows
        }

        var nextFlowRight = flowPos
        while (ground[nextFlowRight.y][nextFlowRight.x] == '|') {
            nextFlowRight = Pos(nextFlowRight.x + 1, nextFlowRight.y)
        }
        var nextFlowLeft = flowPos
        while (ground[nextFlowLeft.y][nextFlowLeft.x] == '|') {
            nextFlowLeft = Pos(nextFlowLeft.x - 1, nextFlowLeft.y)
        }
        if (ground[nextFlowRight.y][nextFlowRight.x] == '.' || ground[nextFlowLeft.y][nextFlowLeft.x] == '.') {
            return emptyList()
        }

        // fill with water
        val backtrackPos = mutableListOf<Pos>()
        backtrackPos.addAll(fillWater(flowPos, Pos(-1, 0)))
        backtrackPos.addAll(fillWater(flowPos, Pos(1, 0)))

        // back track the flow
        return backtrackPos
    }

    private fun fillWater(startPos: Pos, incr: Pos): List<Pos> {
        var pos = startPos
        val backtrackPos = mutableListOf<Pos>()
        do {
            ground[pos.y][pos.x] = '~'

            if (pos.y > 0 && ground[pos.y-1][pos.x] == '|') {
                backtrackPos.add(Pos(pos.x, pos.y - 1))
            }
            pos = Pos(pos.x + incr.x, pos.y)
        } while (ground[pos.y][pos.x] == '|')
        return backtrackPos
    }

    private val xRegex = "x=([0-9.]*)".toRegex()
    private val yRegex = "y=([0-9.]*)".toRegex()

    private fun parseInput() {
        this.javaClass.getResourceAsStream("aoc18/day17/input.txt")
            .bufferedReader().forEachLine {
                val xStr = xRegex.find(it)!!.groups[1]!!.value
                val yStr = yRegex.find(it)!!.groups[1]!!.value

                if (xStr.contains('.')) {
                    val yVal = yStr.toInt()
                    val xFrom = xStr.substring(0, xStr.indexOf('.')).toInt()
                    val xTo =  xStr.substring(xStr.indexOf('.') + 2).toInt()
                    for (xVal in xFrom .. xTo) {
                        ground[yVal][xVal] = '#'
                    }
                    yMax = Math.max(yMax, yVal)
                } else {
                    val xVal = xStr.toInt()
                    val yFrom = yStr.substring(0, yStr.indexOf('.')).toInt()
                    val yTo =  yStr.substring(yStr.indexOf('.') + 2).toInt()
                    for (yVal in yFrom .. yTo) {
                        ground[yVal][xVal] = '#'
                    }
                    yMax = Math.max(yMax, yTo)
                }
            }
    }

    data class Pos(val x: Int, val y: Int)
}