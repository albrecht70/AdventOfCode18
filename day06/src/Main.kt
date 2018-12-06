import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Main {
    var dim = 358
    var threshold = 10000

    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        //teil1()
        teil2()
    }

    class Coord(var x:Int, var y:Int, var id:String? = null, var distance:Int? = null, var distanceTo:String? = null)

    class Coord2(var x:Int, var y:Int, var id:String? = null, var sumDist:Int = 0)

    @Throws(Exception::class)
    private fun teil2() {
        var pointId = 1
        var grid = Array(dim, { val yInd = it;Array(dim, { Coord2(it, yInd)}) })

        //val file = File("day06-test.txt")
        val file = File("day06-input.txt")
        file.forEachLine {
            val idx = it.indexOf(',')
            val x = it.substring(0,idx).toInt()
            val y = it.substring(idx+2).toInt()
            grid[x][y] = Coord2(x, y, "P${pointId}")
            pointId++
        }

        // calc manhattan distance
        for (coord in grid.flatten()) {
            coord.sumDist = grid.flatten()
                    .filter{coord -> (coord.id != null)}
                    .sumBy { p -> (Math.abs(coord.x - p.x) + Math.abs(coord.y - p.y)) }
        }

        val withinSize = grid.flatten().filter{coord -> (coord.sumDist < threshold)}.size

        // print map
        for (row in grid) {
            for (col in row) {
                if (col.sumDist < threshold)
                    print("#")
                else
                    print(".")
            }
            println()
        }

        println("Size: $withinSize")
    }

    @Throws(Exception::class)
    private fun teil1() {
        var pointId = 1
        var grid = Array(dim, { val yInd = it;Array(dim, { Coord(it, yInd)}) })

        //val file = File("day06-test.txt")
        val file = File("day06-input.txt")
        file.forEachLine {
            val idx = it.indexOf(',')
            val x = it.substring(0,idx).toInt()
            val y = it.substring(idx+2).toInt()
            //println("$it : x:$x y:$y")
            grid[x][y] = Coord(x, y, "P${pointId}")
            pointId++
        }

        // calc manhattan distance
        for (coord in grid.flatten().parallelStream()) {
            if (coord.id != null) {
                coord.distance = 0
                coord.distanceTo = "${coord.id}"
            } else {
                var minDist = Int.MAX_VALUE
                var minPoint: String? = null
                for (p in grid.flatten().filter{coord -> (coord.id != null)}) {
                    val dist = Math.abs(coord.x - p.x) + Math.abs(coord.y - p.y)
                    if (minDist > dist) {
                        minDist = dist
                        minPoint = p.id
                    }
                }
                coord.distance = minDist
                coord.distanceTo = "$minPoint"
            }
        }

        for (cell in grid.flatten().groupBy { it.distanceTo }
                .filter { pEntry -> (pEntry.value
                        .find{ c -> (c.x == 0 || c.x == dim-1 || c.y == 0 || c.y == dim-1)}) == null }) {

            println("${cell.key} --> count: ${cell.value.count()}")
        }

        val maxEntry: Map.Entry<String?, List<Coord>>? = grid.flatten().groupBy { it.distanceTo }
                .filter { pEntry ->
                    (pEntry.value
                            .find { c -> (c.x == 0 || c.x == dim - 1 || c.y == 0 || c.y == dim - 1) }) == null
                }.maxBy { it.value.count() }
        println("${maxEntry!!.key} -> #${maxEntry!!.value.count()}")
    }

}