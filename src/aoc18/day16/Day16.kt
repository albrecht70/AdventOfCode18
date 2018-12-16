object Day16 {

    @JvmStatic
    fun main(args: Array<String>) {
        teil1u2()
    }

    private val regexBefore = "Before: \\[(.*)]".toRegex()
    private val regexInstr = "^([0-9].*[0-9])$".toRegex()
    private val regexAfter = "After:  \\[(.*)]".toRegex()

    val samples = mutableListOf<Sample>()
    val instrs = listOf(
        Addr(), Addi(), Mulr(), Muli(),
        Banr(), Bani(), Borr(), Bori(), Setr(), Seti(),
        Gtir(), Gtri(), Gtrr(), Eqir(), Eqri(), Eqrr()
    )

    private fun teil1u2() {
        var currRegexBefore: IntArray? = null
        var currInstr: IntArray? = null
        var isTrainingSet = true
        val program = mutableListOf<IntArray>()

        this.javaClass.getResourceAsStream("aoc18/day16/input.txt")
            .bufferedReader().forEachLine {
                if (isTrainingSet && regexBefore.matches(it)) {
                    currRegexBefore = regexBefore.matchEntire(it)!!.groups[1]!!.value
                        .split(",").stream().mapToInt { reg -> reg.trim().toInt() }.toArray()
                }
                if (regexInstr.matches(it)) {
                    if (isTrainingSet && currRegexBefore == null) {
                        isTrainingSet = false
                    }
                    currInstr = regexInstr.matchEntire(it)!!.groups[0]!!.value
                        .split(" ").stream().mapToInt { reg -> reg.trim().toInt() }.toArray()
                    if (!isTrainingSet) {
                        program.add(currInstr!!)
                    }
                }
                if (isTrainingSet && regexAfter.matches(it)) {
                    val currRegexAfter = regexAfter.matchEntire(it)!!.groups[1]!!.value
                        .split(",").stream().mapToInt { reg -> reg.trim().toInt() }.toArray()
                    samples.add(Sample(currRegexBefore!!, currInstr!!, currRegexAfter))
                    currRegexBefore = null
                }
            }
        println("found samples #${samples.size} and registers #${program.size}")

        val opcodes = mutableMapOf<Int,Instr>()
        while (opcodes.size < 16) {
            var resCount = 0
            samples.forEach { s ->
                val matches = mutableListOf<Instr>()
                for (instr in instrs) {
                    val result = instr.apply(s.regBefore, s.instr)
                    if (s.regAfter.contentEquals(result)) {
                        matches.add(instr)
                    }
                }
                if (matches.size >= 3) {
                    resCount++
                }

                matches.removeIf { m -> opcodes.values.contains(m) }
                if (matches.size == 1) {
                    opcodes[s.instr[0]] = matches[0]
                }
            }
            println("Result count: $resCount")
        }

        var register = intArrayOf(0, 0, 0, 0)
        program.forEach { pi ->
            val instr = opcodes[pi[0]]
            register = instr!!.apply(register, pi)
        }
        println("Register: ${register.asList()}")
    }

    data class Sample(val regBefore: IntArray, val instr: IntArray, val regAfter: IntArray) {

        override fun toString(): String {
            return "${regBefore.asList()} --> ${instr.asList()} --> ${regAfter.asList()}"
        }
    }

    abstract class Instr {
        fun apply(register: IntArray, instr: IntArray): IntArray {
            val result = register.clone()
            result[instr[3]] = exec(register, instr)
            return result
        }

        abstract fun exec(reg: IntArray, instr: IntArray): Int
    }
    class Addr : Instr() {
        override fun exec(reg: IntArray, instr: IntArray): Int = reg[instr[1]] + reg[instr[2]]
    }
    class Addi : Instr() {
        override fun exec(reg: IntArray, instr: IntArray): Int = reg[instr[1]] + instr[2]
    }
    class Mulr : Instr() {
        override fun exec(reg: IntArray, instr: IntArray): Int = reg[instr[1]] * reg[instr[2]]
    }
    class Muli : Instr() {
        override fun exec(reg: IntArray, instr: IntArray): Int = reg[instr[1]] * instr[2]
    }
    class Banr : Instr() {
        override fun exec(reg: IntArray, instr: IntArray): Int = reg[instr[1]] and reg[instr[2]]
    }
    class Bani : Instr() {
        override fun exec(reg: IntArray, instr: IntArray): Int = reg[instr[1]] and instr[2]
    }
    class Borr : Instr() {
        override fun exec(reg: IntArray, instr: IntArray): Int = reg[instr[1]] or reg[instr[2]]
    }
    class Bori : Instr() {
        override fun exec(reg: IntArray, instr: IntArray): Int = reg[instr[1]] or instr[2]
    }
    class Setr : Instr() {
        override fun exec(reg: IntArray, instr: IntArray): Int = reg[instr[1]]
    }
    class Seti : Instr() {
        override fun exec(reg: IntArray, instr: IntArray): Int = instr[1]
    }
    class Gtir : Instr() {
        override fun exec(reg: IntArray, instr: IntArray): Int = if (instr[1] > reg[instr[2]]) 1 else 0
    }
    class Gtri : Instr() {
        override fun exec(reg: IntArray, instr: IntArray): Int = if (reg[instr[1]] > instr[2]) 1 else 0
    }
    class Gtrr : Instr() {
        override fun exec(reg: IntArray, instr: IntArray): Int = if (reg[instr[1]] > reg[instr[2]]) 1 else 0
    }
    class Eqir : Instr() {
        override fun exec(reg: IntArray, instr: IntArray): Int = if (instr[1] == reg[instr[2]]) 1 else 0
    }
    class Eqri : Instr() {
        override fun exec(reg: IntArray, instr: IntArray): Int = if (reg[instr[1]] == instr[2]) 1 else 0
    }
    class Eqrr : Instr() {
        override fun exec(reg: IntArray, instr: IntArray): Int = if (reg[instr[1]] == reg[instr[2]]) 1 else 0
    }
}