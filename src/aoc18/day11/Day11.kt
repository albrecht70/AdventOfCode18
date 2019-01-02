object Day11 {

    // Uses a summed-area table (https://en.wikipedia.org/wiki/Summed-area_table)

    @JvmStatic
    fun main(args: Array<String>) {
        part1()
        part2()
    }

    private val serialNum = 7511
    private val dim = 300

    private fun part1() {
        val summedAreaTable = createSummedAreaTable()

        val squareSize = 3
        val maxPower = calcSquares(squareSize, summedAreaTable).maxBy { it.power }!!

        println("Part1: max total power: $maxPower ==> ${maxPower.x},${maxPower.y}")
    }

    private fun part2() {
        val summedAreaTable = createSummedAreaTable()

        val maxPower = (1..dim).asSequence().flatMap { n ->
            calcSquares(n, summedAreaTable)
        }.maxBy { it.power }!!

        println("Part2: max total power: $maxPower ==> ${maxPower.x},${maxPower.y},${maxPower.size}")
    }

    private fun calcSquares(n: Int, summedAreaTable: Array<IntArray>): Sequence<Square> =
        (n .. dim).asSequence().flatMap { y ->
            (n .. dim).asSequence().map { x ->
                Square(x - n + 1, y - n + 1, calcSquareSum(x, y, n, summedAreaTable), n)
            }
        }

    private fun calcSquareSum(x: Int, y: Int, n: Int, summedAreaTable: Array<IntArray>): Int {
        val lowerRight = summedAreaTable[y][x]
        val upperRight = summedAreaTable[y - n][x]
        val lowerLeft = summedAreaTable[y][x - n]
        val upperLeft = summedAreaTable[y - n][x - n]
        return lowerRight + upperLeft - upperRight - lowerLeft
    }

    private fun createSummedAreaTable(): Array<IntArray> {
        val grid = Array(dim + 1) { IntArray(dim + 1) }

        (0 until (dim + 1)).forEach { y ->
            (0 until (dim + 1)).forEach { x ->
                val cell = calcPowerLevel(x, y)
                val up = if (y == 0) 0 else grid[y - 1][x]
                val left = if (x == 0) 0 else grid[y][x - 1]
                val upperLeft = if (x == 0 || y == 0) 0 else grid[y - 1][x - 1]
                grid[y][x] = cell + up + left - upperLeft
            }
        }
        return grid
    }

    private fun calcPowerLevel(x: Int, y: Int): Int {
        val rackId = x + 10
        return (((((rackId * y) + serialNum) * rackId) / 100) % 10) - 5
    }

    private data class Square(val x: Int, val y: Int, val power: Int, val size: Int)
}