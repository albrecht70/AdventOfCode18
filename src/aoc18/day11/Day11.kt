object Day11 {

    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        teil1u2()
    }

    @Throws(Exception::class)
    private fun teil1u2() {

        val serial = 7511
        val dim = 300
        val cells : Array<Array<Long>> = Array(dim+1, { Array(dim+1, {it -> 0.toLong()}) })

        for (x in 1..dim) {
            for (y in 1..dim) {
                val rackId = x + 10
                var pLevel = ((rackId * y).toLong() + serial) * rackId
                if (pLevel > 99) {
                    pLevel = ((pLevel / Math.pow(10.toDouble(), 2.toDouble()).toInt()) % 10)
                } else {
                    pLevel = 0
                }
                pLevel -= 5
                cells[x][y] = pLevel
            }
        }

        var maxMax = 0L

        val cells2 : Array<Array<Long>> = Array(dim+1, { Array(dim+1, {it -> 0.toLong()}) })
        for (size in 1..dim) {
            for (x in 1..(dim - size + 1)) {
                for (y in 1..(dim - size + 1)) {

                    cells2[x][y] = 0
                    for (xi in 0..(size-1)) {
                        for (yi in 0..(size - 1)) {
                            cells2[x][y] += cells[x + xi][y + yi]
                        }
                    }

                }
            }
            val max = cells2.flatten().max()!!
            if (max > maxMax) {
                for (x in 1..(dim - 2)) {
                    for (y in 1..(dim - 2)) {
                        if (cells2[x][y] == max) {
                            println("$x,$y,$size (Max: $max)")
                        }
                    }
                }
                maxMax = Math.max(maxMax, max)
            }
        }

    }
}