import java.io.File

object Main {

    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        teil1()
        teil2()
    }

    @Throws(Exception::class)
    private fun teil2() {
        var maxSize = 1000
        var ids = ArrayList<String>()
        var fabric = Array(maxSize, {Array<ArrayList<String>>(maxSize, { ArrayList<String>() }) })

        //val file = File("day03-test.txt")
        val file = File("day03-input.txt")
        file.forEachLine {
            val idx1 = it.indexOf('@')
            val idx2 = it.indexOf(',')
            val idx3 = it.indexOf(':')
            val idx4 = it.indexOf('x')

            val id = it.substring(1, idx1-1)
            val xx = it.substring(idx1+2, idx2).toInt()
            val yy = it.substring(idx2+1, idx3).toInt()
            val ww = it.substring(idx3+2, idx4).toInt()
            val hh = it.substring(idx4+1).toInt()

            println("$it --> `$id` $xx,$yy > $ww x $hh")

            ids.add(id)
            for(xPos in xx..(xx + ww - 1)) {
                for(yPos in yy..(yy + hh - 1)) {
                    var used = fabric.get(xPos).get(yPos)
                    used.add(id)
                }
            }
        }
        println("ID count: ${ids.size}")

        for(row in fabric) {
            for(col in row) {
                if (col.size > 1) {
                    for (testId in col) {
                        ids.remove(testId)
                    }
                }
            }
        }

        println("Left over IDs: $ids")
    }

    @Throws(Exception::class)
    private fun teil1() {
        val maxSize = 1000
        var fabric = Array(maxSize, {IntArray(maxSize)})

        //val file = File("day03-test.txt")
        val file = File("day03-input.txt")
        file.forEachLine {
            val idx1 = it.indexOf('@')
            val idx2 = it.indexOf(',')
            val idx3 = it.indexOf(':')
            val idx4 = it.indexOf('x')

            val id = it.substring(1, idx1-1)
            val xx = it.substring(idx1+2, idx2).toInt()
            val yy = it.substring(idx2+1, idx3).toInt()
            val ww = it.substring(idx3+2, idx4).toInt()
            val hh = it.substring(idx4+1).toInt()

            println("$it --> `$id` $xx,$yy > $ww x $hh")

            for(xPos in xx..(xx + ww - 1)) {
                for(yPos in yy..(yy + hh - 1)) {
                    fabric.get(xPos).set(yPos, 1 + fabric.get(xPos).get(yPos))
                }
            }
        }

        var count = 0
        for(row in fabric) {
            for(col in row) {
                if (col > 1) {
                    count++
                }
            }
        }
        println("Count: $count")
    }

}
