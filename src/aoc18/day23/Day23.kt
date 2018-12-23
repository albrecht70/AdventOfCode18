import kotlin.math.abs
import kotlin.math.max

object Day23 {

    @JvmStatic
    fun main(args: Array<String>) {
        teil1()
        teil2()
    }

    val bots = mutableListOf<Bot>()

    private fun teil2() {
        parseInput()

        var radius = bots.fold(0) { r, b -> max(r, max(abs(b.p.x), max(abs(b.p.y), abs(b.p.z))))}
        val startPos = Pos(0,0,0)

        var candidates = setOf(Bot(startPos, radius))
        while (radius > 0) {
            radius /= 2

            val counts = mutableMapOf<Bot, Int>()
            candidates.forEach { bot ->
                bot.p.neighbors(radius).map { pos ->
                    counts[Bot(pos, radius)] = bots.count { b -> manhDist(b.p, pos) <= b.r }
                }
            }
            val max = counts.maxBy { e -> e.value }!!
            candidates = counts.filter { e -> e.value == max.value }.map { e -> e.key }.toSet()

            if (candidates.size > 10000) {
                candidates = candidates.take(10000).toSet()
            }

            println("r: $radius - max: $max - counts: #${counts.size} - candidates: #${candidates.count()}")
        }

        val resultDist = manhDist(candidates.minBy { c -> manhDist(c.p, startPos) }!!.p, startPos)
        println("--> $resultDist")
    }

    private fun teil1() {
        parseInput()

        val maxBot = bots.maxBy { b -> b.r }!!
        val count = bots.filter { b -> manhDist(b.p, maxBot.p) <= maxBot.r }.count()

        println("Count: $count")
    }

    private fun manhDist(p1: Pos, p2: Pos) =
        Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y) + Math.abs(p1.z - p2.z)

    private val lineRegex = "pos=<([-0-9]*),([-0-9]*),([-0-9]*)>, r=([0-9]*)".toRegex()

    private fun parseInput() {
        bots.clear()
        this.javaClass.getResourceAsStream("aoc18/day23/input.txt")
            .bufferedReader().forEachLine { line ->
                val mgroups = lineRegex.matchEntire(line)!!.groups
                bots.add(Bot(Pos(mgroups[1]!!.value.toInt(), mgroups[2]!!.value.toInt(),
                    mgroups[3]!!.value.toInt()), mgroups[4]!!.value.toInt()))
            }
    }

    data class Bot(val p: Pos, val r: Int)

    data class Pos(var x: Int, var y: Int, var z: Int) {

        fun neighbors(delta: Int): Iterable<Pos> =
            (-1..1).flatMap { xd ->
                (-1..1).flatMap { yd ->
                    (-1..1).map { zd ->
                        Pos(this.x + xd * delta,this.y + yd * delta,this.z + zd * delta)
                    }
                }
            }
    }
}