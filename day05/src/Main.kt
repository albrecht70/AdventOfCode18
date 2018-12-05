import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Main {

    private var charDiff = 'a'.toInt() - 'A'.toInt()

    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        teil1()
        teil2()
    }

    @Throws(Exception::class)
    private fun teil2() {

        //val file = File("day05-test.txt")
        val file = File("day05-input.txt")
        file.forEachLine {

            var minCount = Int.MAX_VALUE
            for (ch in 'a'.toInt()..'z'.toInt()) {
                var input = it.filter { c -> (c.toInt() != ch)}.filter { c -> (c.toInt() != (ch - charDiff)) }
                var currInput = ""
                var nextInput = input
                while (!currInput.equals(nextInput)) {
                    currInput = nextInput
                    nextInput = reactRec(currInput)
                    //println("=> $currInput / $nextInput")
                }
                //println("Char count: ${nextInput.length}" )
                minCount = Math.min(minCount, nextInput.length)
            }
            println("Min count: $minCount")
        }
    }

    @Throws(Exception::class)
    private fun teil1() {

        //val file = File("day05-test.txt")
        val file = File("day05-input.txt")
        file.forEachLine {
            var currInput = ""
            var nextInput = it
            while (!currInput.equals(nextInput)) {
                currInput = nextInput
                nextInput = reactRec(currInput)
                //println("=> $currInput / $nextInput")
            }
            println("Char count: ${nextInput.length}" )
        }
    }

    private fun reactRec(input: String): String {
        var nextInput = StringBuilder()

        var lastChar: Int? = null
        for (ch in input.chars()) {
            if (lastChar != null && Math.abs(lastChar - ch) == charDiff) {
                lastChar = null
            } else {
                if (lastChar != null) {
                    nextInput.append(lastChar.toChar())
                }
                lastChar = ch
            }
        }
        if (lastChar != null) {
            nextInput.append(lastChar.toChar())
        }
        return nextInput.toString()
    }

}