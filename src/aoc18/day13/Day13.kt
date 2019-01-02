object Day13 {

    @JvmStatic
    fun main(args: Array<String>) {
        part1and2()
    }

    val dim = 150
    val roadMap = Array(dim) { CharArray(dim) { ' ' } }
    val cars = mutableListOf<Car>()

    private fun part1and2() {
        parseInput()

        var tick = 0
        var firstCrash = true
        while (cars.count { car -> !car.crashed } > 1) {
            cars.sortWith(compareBy<Car> { it.yPos }.thenBy {it.xPos})

            cars.asSequence().filterNot { it.crashed }.forEach { car ->
                var newX = car.xPos
                var newY = car.yPos
                when (car.direction) {
                    '>' -> newX += 1
                    '<' -> newX -= 1
                    '^' -> newY -= 1
                    'v' -> newY += 1
                }
                val roadChar = roadMap[newY][newX]
                car.xPos = newX
                car.yPos = newY

                when (roadChar) {
                    '/' -> when (car.direction) {
                                '>' -> car.direction = '^'
                                '<' -> car.direction = 'v'
                                '^' -> car.direction = '>'
                                'v' -> car.direction = '<'
                            }
                    '\\' -> when (car.direction) {
                                '>' -> car.direction = 'v'
                                '<' -> car.direction = '^'
                                '^' -> car.direction = '<'
                                'v' -> car.direction = '>'
                            }
                    '+' -> if (car.direction == 'v' && car.nextTurn == 'l'
                                || car.direction == '^' && car.nextTurn == 'r'
                                || car.direction == '>' && car.nextTurn == 's'
                            ) {
                                car.direction = '>'
                                car.nextTurn()
                            } else if (car.direction == 'v' && car.nextTurn == 'r'
                                || car.direction == '^' && car.nextTurn == 'l'
                                || car.direction == '<' && car.nextTurn == 's'
                            ) {
                                car.direction = '<'
                                car.nextTurn()
                            } else if (car.direction == '^' && car.nextTurn == 's'
                                || car.direction == '>' && car.nextTurn == 'l'
                                || car.direction == '<' && car.nextTurn == 'r'
                            ) {
                                car.direction = '^'
                                car.nextTurn()
                            } else if (car.direction == 'v' && car.nextTurn == 's'
                                || car.direction == '>' && car.nextTurn == 'r'
                                || car.direction == '<' && car.nextTurn == 'l'
                            ) {
                                car.direction = 'v'
                                car.nextTurn()
                            }
                }

                val crashCar = cars.filter { !it.crashed && it != car}
                    .find { it.xPos == car.xPos && it.yPos == car.yPos }

                if (crashCar != null) {
                    if (firstCrash) {
                        println("Part1: crash location: ${crashCar.xPos},${crashCar.yPos}")
                        firstCrash = false
                    }
                    car.crashed = true
                    crashCar.crashed = true
                }
            }

            tick++
        }

        val lastCar = cars.filter { car -> !car.crashed }.first()
        println("Part2: location: ${lastCar.xPos},${lastCar.yPos}")
    }

    private fun parseInput() {
        var row = 0
        this.javaClass.getResourceAsStream("aoc18/day13/input.txt")
            .bufferedReader().forEachLine { line ->
                roadMap[row++] = line.toCharArray()
            }

        roadMap.forEachIndexed { yIdx, chars ->
            for (ch in "><^v") {
                val xIdx = chars.indexOf(ch)
                if (xIdx > -1) {
                    cars.add(Car(xIdx, yIdx, ch))
                }
            }
        }

    }

    data class Car(
        var xPos: Int,
        var yPos: Int,
        var direction: Char,
        var nextTurn: Char = 'l',
        var crashed: Boolean = false
    ) {

        fun nextTurn() {
            nextTurn = when (nextTurn) {
                'l' -> 's'
                's' -> 'r'
                'r' -> 'l'
                else  -> '?'
            }
        }
    }
}