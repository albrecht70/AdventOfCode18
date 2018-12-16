import java.util.*
import kotlin.collections.ArrayList

object Day15 {

    @JvmStatic
    fun main(args: Array<String>) {
        teil1u2()
    }

    data class Pos(val xPos: Int, val yPos: Int)

    data class Creature(
        val type: Char,
        var pos: Pos,
        var hitpoints: Int = 200,
        var isAlive: Boolean = true
    )

    val map = Array(33) { CharArray(33) { ' ' } }
    val creatures = ArrayList<Creature>()
    var elfAttacKPower = 3

    private fun teil1u2() {
        do {
            var row = 0
            creatures.clear()
            this.javaClass.getResourceAsStream("aoc18/day15/input.txt")
                .bufferedReader().forEachLine {
                    map[row++] = it.toCharArray()
                }

            map.forEachIndexed { yIdx, chars ->
                for (ch in "EG") {

                    val rowStr = String(chars)
                    var xIdx = rowStr.indexOf(ch)
                    while (xIdx > -1) {
                        creatures.add(Creature(ch, Pos(xIdx, yIdx)))
                        xIdx = rowStr.indexOf(ch, xIdx + 1)
                    }
                }
            }

            var turn = 0
            do {
                creatures.sortBy { c -> (c.pos.yPos * map.size + c.pos.xPos) }

                for (cr in creatures) {
                    if (cr.isAlive) {
                        var crToAttack = canAttack(cr)
                        if (crToAttack != null) {
                            attack(cr, crToAttack)
                        } else {
                            move(cr)
                            crToAttack = canAttack(cr)
                            if (crToAttack != null) {
                                attack(cr, crToAttack)
                            }
                        }
                    }
                }

                turn++
                //printMap(turn)
            } while (creatures.filter { it.type == 'E' }.count { it.isAlive } > 0 &&
                creatures.filter { it.type == 'G' }.count { it.isAlive } > 0)

            turn--
            var sumHitpoints = creatures.filter { c -> c.isAlive }.sumBy { c -> c.hitpoints }
            println("$turn * $sumHitpoints = ${turn * sumHitpoints}  (elf attack power: $elfAttacKPower)")

            elfAttacKPower++
        } while (creatures.filter { it.type == 'E' }.count { !it.isAlive } > 0)
    }

    private fun move(cr: Creature) {
        val posSet = HashSet<Pos>()
        creatures.filter { it.type != cr.type && it.isAlive }.forEach { cr ->
            posSet.add(Pos(cr.pos.xPos + 1, cr.pos.yPos))
            posSet.add(Pos(cr.pos.xPos, cr.pos.yPos + 1))
            posSet.add(Pos(cr.pos.xPos - 1, cr.pos.yPos))
            posSet.add(Pos(cr.pos.xPos, cr.pos.yPos - 1))
        }
        posSet.removeIf { p -> map[p.yPos][p.xPos] != '.' }

        val paths = posSet.associateBy({ it }, { p -> findPath(cr.pos, p) })
        val shortest = paths.entries
            .filter { it.value.isNotEmpty() }
            .sortedBy { e -> (e.key.yPos * map.size + e.key.xPos) }
            .minBy { it.value.size }

        if (shortest != null) {
            val nextPos = shortest.value[0]

            map[cr.pos.yPos][cr.pos.xPos] = '.'
            cr.pos = nextPos
            map[cr.pos.yPos][cr.pos.xPos] = cr.type
        }
    }

    data class SearchNode(val pos: Pos, val prev: SearchNode?)

    private fun findPath(from: Pos, to: Pos): List<Pos> {
        val visited = mutableListOf<Pair<Pos, Pos>>()
        val queue = LinkedList<SearchNode>()
        val shortestPaths = mutableListOf<List<Pos>>()

        queue.add(SearchNode(from, null))
        while (queue.isNotEmpty()) {
            val sn = queue.remove()

            var snLevel = 0
            var rn = sn
            while (rn != null) {
                snLevel++
                rn = rn.prev
            }

            if (shortestPaths.isNotEmpty() && snLevel > shortestPaths.first().size) {
                shortestPaths.sortBy { p -> p.first().yPos * map.size + p.first().xPos }
                return shortestPaths.first()
            }

            for (next in listOf(
                Pos(sn.pos.xPos, sn.pos.yPos - 1),
                Pos(sn.pos.xPos - 1, sn.pos.yPos),
                Pos(sn.pos.xPos + 1, sn.pos.yPos),
                Pos(sn.pos.xPos, sn.pos.yPos + 1)
            )) {
                if (next == to) {
                    val path = mutableListOf(next)
                    var rn = sn
                    while (rn != null) {
                        if (rn.prev != null) {
                            path.add(0, rn.pos)
                        }
                        rn = rn.prev
                    }
                    shortestPaths.add(path)
                }
                if (!visited.contains(Pair(sn.pos, next)) && map[next.yPos][next.xPos] == '.') {
                    queue.add(SearchNode(next, sn))
                    visited.add(Pair(sn.pos, next))
                }
            }
        }
        return emptyList()
    }

    private fun canAttack(cr: Creature): Creature? {
        val posList = ArrayList<Pos>()
        posList.add(Pos(cr.pos.xPos + 1, cr.pos.yPos))
        posList.add(Pos(cr.pos.xPos, cr.pos.yPos + 1))
        posList.add(Pos(cr.pos.xPos - 1, cr.pos.yPos))
        posList.add(Pos(cr.pos.xPos, cr.pos.yPos - 1))

        val crToAttack =
            posList.map { p -> creatures.find { attackCr -> attackCr.pos == p && attackCr.type != cr.type && attackCr.isAlive } }
                .filter { c -> c != null }
                .sortedBy { c -> (c!!.pos.yPos * map.size + c!!.pos.xPos) }
                .minBy { c -> c!!.hitpoints }
        return crToAttack
    }

    private fun attack(cr: Creature, crToAttack: Creature) {
        if (cr.type == 'E') {
            crToAttack.hitpoints -= elfAttacKPower
        } else {
            crToAttack.hitpoints -= 3
        }
        if (crToAttack.hitpoints <= 0) {
            println("$crToAttack died!")
            crToAttack.isAlive = false
            map[crToAttack.pos.yPos][crToAttack.pos.xPos] = '.'
        }
    }

    private fun printMap(turn: Int) {
        println("Round $turn")
        map.forEach { chars ->
            println(chars)
        }
    }
}