object Day19 {

    @JvmStatic
    fun main(args: Array<String>) {
        part1()
        part2()
    }

    private var ipReg = 0
    private val code = mutableListOf<Instr>()

    private fun part1() {
        parseInput()

        val register = intArrayOf(0, 0, 0, 0, 0, 0)
        var ip = 0
        while (ip >= 0 && ip < code.size) {
            val instr = code[ip]

            register[ipReg] = ip
            val op = instrDef[instr.opCode]!!
            register[instr.c] = op.invoke(instr.a, instr.b, register)
            ip = register[ipReg]

            ip++
        }
        println("Part1: ${register[0]}")
    }

    private fun part2() {
        parseInput()

        val register = intArrayOf(1, 0, 0, 0, 0, 0)
        var ip = 0
        var tick = 0L
        while (ip >= 0 && ip < code.size) {

            if (ip == 2 && register[1] != 0) {
                if (register[4] % register[1] == 0) {
                    register[0] += register[1]
                }
                ip = 12
                continue
            }

            val instr = code[ip]
            register[ipReg] = ip
            val op = instrDef[instr.opCode]!!
            register[instr.c] = op.invoke(instr.a, instr.b, register)
            ip = register[ipReg]

            ip++
            tick++
        }
        println("Part2: ${register[0]}")
    }

    private fun parseInput() {
        this.javaClass.getResourceAsStream("aoc18/day19/input.txt")
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

    private val instrDef = mapOf<String, (Int, Int, IntArray) -> Int>(
        "addr" to { a, b, r -> r[a] + r[b] },
        "addi" to { a, b, r -> r[a] + b },
        "mulr" to { a, b, r -> r[a] * r[b] },
        "muli" to { a, b, r -> r[a] * b },
        "banr" to { a, b, r -> r[a] and r[b] },
        "bani" to { a, b, r -> r[a] and b },
        "borr" to { a, b, r -> r[a] or r[b] },
        "bori" to { a, b, r -> r[a] or b },
        "setr" to { a, _, r -> r[a] },
        "seti" to { a, _, _ -> a },
        "gtir" to { a, b, r -> if (a > r[b]) 1 else 0 },
        "gtri" to { a, b, r -> if (r[a] > b) 1 else 0 },
        "gtrr" to { a, b, r -> if (r[a] > r[b]) 1 else 0 },
        "eqir" to { a, b, r -> if (a == r[b]) 1 else 0 },
        "eqri" to { a, b, r -> if (r[a] == b) 1 else 0 },
        "eqrr" to { a, b, r -> if (r[a] == r[b]) 1 else 0 }
    )
}