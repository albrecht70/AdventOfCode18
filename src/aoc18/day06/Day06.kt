object Day06 {

    @JvmStatic
    fun main(args: Array<String>) {
        part1()
        part2()
    }

    private fun part1() {
        val coords = parseInput()
        val dimx = coords.maxBy { c -> c.x }!!.x
        val dimy = coords.maxBy { c -> c.y }!!.y

        val edgeCoords = mutableSetOf<Coord>()
        val result = (0..dimy).flatMap { y ->
            (0..dimx).map { x ->
                val minCoords = coords.map { c -> c to c.manhDist(x, y) }
                    .sortedBy { it.second }
                    .take(2)
                if (x == 0 || y == 0 || x == dimx || y == dimy) {
                    edgeCoords.add(minCoords[0].first)
                }
                minCoords[0].first.takeUnless { minCoords[0].second == minCoords[1].second }
            }
        }.filterNot { it in edgeCoords }
            .groupingBy { it }
            .eachCount()
            .maxBy { it.value }!!
            .value
        println("Part1: $result")
    }

    private fun part2() {
        val coords = parseInput()
        val dimx = coords.maxBy { c -> c.x }!!.x
        val dimy = coords.maxBy { c -> c.y }!!.y

        val result = (0..dimy).flatMap { y ->
            (0..dimx).map { x ->
                coords.map { c -> c.manhDist(x, y) }.sum()
            }
        }.filter { it < 10000 }
            .count()

        println("Part2: $result")
    }

    private fun parseInput(): List<Coord> {
        val coords = mutableListOf<Coord>()
        this.javaClass.getResourceAsStream("aoc18/day06/input.txt")
            .bufferedReader().forEachLine { line ->
                val idx = line.indexOf(',')
                val x = line.substring(0, idx).toInt()
                val y = line.substring(idx + 2).toInt()
                coords.add(Coord(x, y))
            }
        return coords
    }

    data class Coord(var x: Int, var y: Int) {

        fun manhDist(cx: Int, cy: Int) =
            Math.abs(x - cx) + Math.abs(y - cy)
    }

}