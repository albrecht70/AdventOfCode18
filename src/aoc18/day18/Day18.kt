object Day18 {

    @JvmStatic
    fun main(args: Array<String>) {
        part1()
        part2()
    }

    private const val dim = 50

    private fun part1() {
        var forrest = parseInput()

        var tick = 0
        do {
            forrest = nextGen(forrest)
            tick++
        } while (tick < 10)

        val countWood = forrest.flatten().count { c -> c == '|' }
        val countLumber = forrest.flatten().count { c -> c == '#' }

        println("Part1: $countWood * $countLumber = ${countWood * countLumber}")
    }

    private fun part2() {
        var forrest = parseInput()

        val counts = mutableListOf<Int>()
        var repeat = 0
        var tick = 0
        do {
            forrest = nextGen(forrest)

            val count = forrest.flatten().count { it == '|' } * forrest.flatten().count { it == '#' }

            val idx = counts.indexOf(count)
            if (idx > -1) {
                val refIdx = ((1000000000 - idx) % (counts.size - idx)) + idx - 1

                repeat++
                if (repeat > 10) {
                    println("Part2: ${counts[refIdx]}")
                    break
                }
            } else {
                repeat = 0
            }
            counts.add(count)

            tick++
        } while (tick < 1000000000)
    }

    private fun nextGen(forrest: Array<Array<Char>>): Array<Array<Char>> {
        val next = Array(dim) { Array(dim) { ' ' } }
        for (y in 0 until dim) {
            for (x in 0 until dim) {

                val neighbours = mutableListOf<Char>()
                for (incry in listOf(-1, 0, 1)) {
                    for (incrx in listOf(-1, 0, 1)) {
                        if ((incrx == 0 && incry == 0)
                            || (y + incry == dim || y + incry < 0)
                            || (x + incrx == dim || x + incrx < 0)
                        ) {
                            continue
                        }
                        neighbours.add(forrest[y + incry][x + incrx])
                    }
                }

                if (forrest[y][x] == '.' && neighbours.count { it == '|' } >= 3) {
                    next[y][x] = '|'
                } else {
                    if (forrest[y][x] == '|' && neighbours.count { it == '#' } >= 3) {
                        next[y][x] = '#'
                    } else {
                        if (forrest[y][x] == '#') {
                            if (neighbours.count { it == '#' } >= 1 && neighbours.count { it == '|' } >= 1) {
                                next[y][x] = '#'
                            } else {
                                next[y][x] = '.'
                            }
                        } else {
                            next[y][x] = forrest[y][x]
                        }
                    }
                }
            }
        }
        return next
    }

    private fun parseInput(): Array<Array<Char>> {
        var row = 0
        val forrest = Array(dim) { Array(dim) { ' ' } }

        this.javaClass.getResourceAsStream("aoc18/day18/input.txt")
            .bufferedReader().forEachLine {
                forrest[row++] = it.toCharArray().toTypedArray()
            }
        return forrest
    }
}