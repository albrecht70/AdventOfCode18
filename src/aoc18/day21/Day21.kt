object Day21 {

    @JvmStatic
    fun main(args: Array<String>) {
        teil1u2()
    }

    val instrDef = mapOf<String, (Int, Int, IntArray) -> Int>(
        "addr" to { a, b, r -> r[a] + r[b] },
        "addi" to { a, b, r -> r[a] + b },
        "mulr" to { a, b, r -> r[a] * r[b] },
        "muli" to { a, b, r -> r[a] * b },
        "banr" to { a, b, r -> r[a] and r[b] },
        "bani" to { a, b, r -> r[a] and b },
        "borr" to { a, b, r -> r[a] or r[b] },
        "bori" to { a, b, r -> r[a] or b },
        "setr" to { a, b, r -> r[a] },
        "seti" to { a, b, r -> a },
        "gtir" to { a, b, r -> if (a > r[b]) 1 else 0 },
        "gtri" to { a, b, r -> if (r[a] > b) 1 else 0 },
        "gtrr" to { a, b, r -> if (r[a] > r[b]) 1 else 0 },
        "eqir" to { a, b, r -> if (a == r[b]) 1 else 0 },
        "eqri" to { a, b, r -> if (r[a] == b) 1 else 0 },
        "eqrr" to { a, b, r -> if (r[a] == r[b]) 1 else 0 }
    )

    private var ipReg = 0
    private val code = mutableListOf<Instr>()

    private fun teil1u2() {
        parseInput()

        val register = intArrayOf(0, 0, 0, 0, 0, 0)
        var ip = 0
        val visited = mutableSetOf<Int>()
        while (ip >= 0 && ip < code.size) {
            val instr = code[ip]
            if (instr.opCode == "eqrr" && !visited.add(register[1])) {
                println("Part 1: ${visited.first()}")
                println("Part 2: ${visited.last()}")
                return
            }

            register[ipReg] = ip
            val op = instrDef[instr.opCode]!!
            register[instr.c] = op.invoke(instr.a, instr.b, register)
            ip = register[ipReg]
            ip++
        }
    }

    private fun teil1BruteForce() {
        parseInput()

        val states = mutableMapOf<Int,State>()
        val tickIter = 100
        var foundTerminated = false
        while (!foundTerminated) {
            val ms1 = System.currentTimeMillis()

            for (reg0 in 0..5000000) {
                val state = states.getOrDefault(reg0, State(0, intArrayOf(reg0, 0, 0, 0, 0, 0), 0))
                val register = state.register
                var ip = state.ip
                var tick = 0
                while (ip >= 0 && ip < code.size && tick < tickIter) {
                    val instr = code[ip]

                    register[ipReg] = ip
                    val op = instrDef[instr.opCode]!!
                    register[instr.c] = op.invoke(instr.a, instr.b, register)
                    ip = register[ipReg]

                    ip++
                    tick++
                }
                if (tick < tickIter) {
                    println("tick: ${tick + state.tick} => ${register.asList()} ==> ${register[0]}")
                    foundTerminated = true
                } else {
                    states.put(reg0, State(ip, register, state.tick + tick))
                }
            }
            val ms2 = System.currentTimeMillis()
            println("tickIter[${ms2-ms1}ms]")
        }
    }

    private fun parseInput() {
        this.javaClass.getResourceAsStream("aoc18/day21/input.txt")
            .bufferedReader().forEachLine {
                if (it.startsWith("#ip ")) {
                    ipReg = it.substring("#ip ".length).toInt()
                } else {
                    val str = it.split(" ")
                    code.add(Instr(str[0], str[1].toInt(), str[2].toInt(), str[3].toInt()))
                }
            }
    }

    data class Instr(val opCode: String, val a: Int, val b: Int, val c: Int)

    data class State(val ip: Int, val register: IntArray, val tick: Int)
}