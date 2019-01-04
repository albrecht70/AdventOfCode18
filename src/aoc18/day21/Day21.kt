object Day21 {

    @JvmStatic
    fun main(args: Array<String>) {
        part1and2()
    }

    private var ipReg = 0
    private val code = mutableListOf<Instr>()

    private fun part1and2() {
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