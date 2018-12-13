object Day13 {

    @JvmStatic
    fun main(args: Array<String>) {
        teil1u2()
    }

    data class Car(
        var xPos: Int,
        var yPos: Int,
        var direction: Char,
        var nextTurn: Char = 'l',
        var crashed: Boolean = false
    )

    val cars = ArrayList<Car>()
    val roadMap = Array(150) { CharArray(150) { ' ' } }

    private fun teil1u2() {

        var row = 0
        this.javaClass.getResourceAsStream("aoc18/day13/input.txt")
            .bufferedReader().forEachLine {
                roadMap[row++] = it.toCharArray()
            }

        roadMap.forEachIndexed { yIdx, chars ->
            for (ch in "><^v") {
                val xIdx = chars.indexOf(ch)
                if (xIdx > -1) {
                    cars.add(Car(xIdx, yIdx, ch))
                }
            }
        }

        println("#${cars.size} cars")

        var tick = 0
        while (cars.count { car -> !car.crashed } > 1) {
            cars.sortWith(Comparator { c1, c2 ->
                when {
                    c1.yPos > c2.yPos -> 1
                    c1.yPos == c2.yPos && c1.xPos > c2.xPos -> 1
                    c1.xPos == c2.xPos && c1.yPos == c2.yPos -> 0
                    else -> -1
                }
            })

            for (car in cars) {
                if (car.crashed) {
                    continue
                }

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
                                nextTurn(car)
                            } else if (car.direction == 'v' && car.nextTurn == 'r'
                                || car.direction == '^' && car.nextTurn == 'l'
                                || car.direction == '<' && car.nextTurn == 's'
                            ) {
                                car.direction = '<'
                                nextTurn(car)
                            } else if (car.direction == '^' && car.nextTurn == 's'
                                || car.direction == '>' && car.nextTurn == 'l'
                                || car.direction == '<' && car.nextTurn == 'r'
                            ) {
                                car.direction = '^'
                                nextTurn(car)
                            } else if (car.direction == 'v' && car.nextTurn == 's'
                                || car.direction == '>' && car.nextTurn == 'r'
                                || car.direction == '<' && car.nextTurn == 'l'
                            ) {
                                car.direction = 'v'
                                nextTurn(car)
                            }
                }

                val crashCar = cars.filter { !it.crashed && it != car}
                    .find { it.xPos == car.xPos && it.yPos == car.yPos }

                if (crashCar != null) {
                    println("Crash: $crashCar and $car at tick $tick")
                    car.crashed = true
                    crashCar.crashed = true
                }
            }

            tick++
        }

        val lastCar = cars.filter { car -> !car.crashed }.first()
        println("Remaining car is at position: [${lastCar.xPos},${lastCar.yPos}]")
    }

    private fun nextTurn(car: Car) {
        car.nextTurn = when (car.nextTurn) {
            'l' -> 's'
            's' -> 'r'
            'r' -> 'l'
            else  -> '?'
        }
    }

}