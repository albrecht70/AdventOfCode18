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

        val scores = IntArray(players)
        val circle = ArrayList<Int>()
        circle.add(0)
        var curr = 0

        for (marble in 1..marbles) {
            var player = ((marble-1) % players) + 1
            if (marble % 23 == 0) {
                curr = ((curr - 6) + circle.size ) % circle.size
                val rmMarble = circle.removeAt(curr)
                curr--
                scores[player-1] += marble + rmMarble
            } else {
                curr = (curr + 2) % circle.size
                circle.add(curr + 1, marble)
            }
        }
        println(" => Max score: ${scores.max()}")
    }

}