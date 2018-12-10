import java.util.*

object Day09 {

    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        teil1u2()
    }

    @Throws(Exception::class)
    private fun teil1u2() {

        this.javaClass.getResourceAsStream("aoc18/day09/input.txt")
            .bufferedReader().forEachLine {
                val players = Regex("^([0-9]+) ").find(it)!!.groups[1]!!.value
                val marbles = Regex("worth ([0-9]+) points").find(it)!!.groups[1]!!.value

                play(players.toInt(), marbles.toInt())
        }
    }

    private fun play(players: Int, marbles: Int) {
        print("players: $players - marbles: $marbles")

        val scores = LongArray(players)
        val circle = Circle()

        for (marble in 1..marbles) {
            var player = ((marble-1) % players) + 1
            if (marble % 23 == 0) {
                val rmVal = circle.prev(7).remove()
                scores[player-1] += marble.toLong() + rmVal.toLong()
            } else {
                circle.next().insertAfter(Element(marble))
            }
            //println("circle: $circle")
        }
        println(" => Max score: ${scores.max()}")
    }

    class Element(val value: Int, var next: Element? = null, var prev: Element? = null) {
        override fun toString(): String {
            return "[$value]"
        }
    }

    class Circle {

        var curr: Element = Element(0)

        init {
            curr.next = curr
            curr.prev = curr
        }

        fun next(): Circle {
            curr = curr.next!!
            return this
        }

        fun prev(count: Int = 1): Circle {
            for (cc in 1..count) {
                curr = curr.prev!!
            }
            return this
        }

        fun insertAfter(newElem: Element): Circle {
            newElem.next = curr.next
            newElem.prev = curr
            curr.next!!.prev = newElem
            curr.next = newElem
            curr = newElem
            return this
        }

        fun remove(): Int {
            val value = curr.value
            curr.prev!!.next = curr.next
            curr.next!!.prev = curr.prev
            curr = curr.next!!
            return value
        }

        override fun toString(): String {
            val startEl = curr;
            var el = curr;

            var str  = "["
            do {
                str += "$el "
                el = el.next!!
            } while (!el.equals(startEl))
            str += "]"
            return str
        }
    }
}