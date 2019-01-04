import java.util.*

object Day15 {

    @JvmStatic
    fun main(args: Array<String>) {
        val ms1 = System.currentTimeMillis()
        part1and2()
        val ms2 = System.currentTimeMillis()
        println("Time: ${ms2 - ms1}")
    }

    private const val dim = 33
    private val map = Array(dim) { CharArray(dim) { ' ' } }
    private val creatures = mutableListOf<Creature>()
    private var elfAttackPower = 3

    private fun part1and2() {
        do {
            parseInput()
            creatures.clear()

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
            } while (creatures.filter { it.type == 'E' }.count { it.isAlive } > 0 &&
                creatures.filter { it.type == 'G' }.count { it.isAlive } > 0)

            turn--
            val sumHitpoints = creatures.filter { c -> c.isAlive }.sumBy { c -> c.hitpoints }
            println("$turn * $sumHitpoints = ${turn * sumHitpoints}  (elf attack power: $elfAttackPower)")

            if (elfAttackPower == 3) {
                println("Part1: $turn * $sumHitpoints (elf attack power: $elfAttackPower) ==> ${turn * sumHitpoints}")
            } else if (creatures.filter { it.type == 'E' }.count { !it.isAlive } == 0) {
                println("Part2: $turn * $sumHitpoints (elf attack power: $elfAttackPower) ==> ${turn * sumHitpoints}")
            }

            elfAttackPower++
        } while (creatures.filter { it.type == 'E' }.count { !it.isAlive } > 0)
    }

    private fun move(cr: Creature) {
        val posSet = HashSet<Pos>()
        creatures.filter { it.type != cr.type && it.isAlive }.forEach { posSet.addAll(it.pos.neighbours()) }
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

    data class SearchNode(val pos: Pos, val path: List<SearchNode>)

    private fun findPath(from: Pos, to: Pos): List<Pos> {
        val visited = mutableListOf<Pair<Pos, Pos>>()
        val queue = ArrayDeque<SearchNode>()
        val shortestPaths = mutableListOf<List<Pos>>()

        queue.add(SearchNode(from, emptyList()))
        while (queue.isNotEmpty()) {
            val sn = queue.remove()

            if (shortestPaths.isNotEmpty() && sn.path.size > shortestPaths.first().size - 2) {
                shortestPaths.sortBy { p -> p.first().yPos * map.size + p.first().xPos }
                return shortestPaths.first()
            }

            for (next in sn.pos.neighbours()) {
                if (next == to) {
                    val path = sn.path.map { it.pos }.toMutableList()
                    if (sn.pos != from) {
                        path.add(sn.pos)
                    }
                    path.add(next)
                    shortestPaths.add(path)
                }
                if (map[next.yPos][next.xPos] == '.' && !visited.contains(Pair(sn.pos, next))) {
                    val pp = sn.path.toMutableList()
                    if (sn.pos != from) {
                        pp.add(sn)
                    }
                    queue.add(SearchNode(next, pp))
                    visited.add(Pair(sn.pos, next))
                }
            }
        }
        return emptyList()
    }

    private fun canAttack(cr: Creature): Creature? {
        val posList = cr.pos.neighbours()
        return posList.map { p -> creatures.find { attackCr -> attackCr.pos == p && attackCr.type != cr.type && attackCr.isAlive } }
            .filter { c -> c != null }
            .sortedBy { c -> (c!!.pos.yPos * map.size + c!!.pos.xPos) }
            .minBy { c -> c!!.hitpoints }
    }

    private fun attack(cr: Creature, crToAttack: Creature) {
        if (cr.type == 'E') {
            crToAttack.hitpoints -= elfAttackPower
        } else {
            crToAttack.hitpoints -= 3
        }
        if (crToAttack.hitpoints <= 0) {
            crToAttack.isAlive = false
            map[crToAttack.pos.yPos][crToAttack.pos.xPos] = '.'
        }
    }

    private fun parseInput() {
        var row = 0
        this.javaClass.getResourceAsStream("aoc18/day15/input.txt")
            .bufferedReader().forEachLine { line ->
                map[row++] = line.toCharArray()
            }

    }

    data class Pos(val xPos: Int, val yPos: Int) {

        fun neighbours(): List<Pos> =
            mutableListOf(
                Pos(xPos, yPos - 1),
                Pos(xPos - 1, yPos),
                Pos(xPos + 1, yPos),
                Pos(xPos, yPos + 1)
            )
    }

    data class Creature(
        val type: Char,
        var pos: Pos,
        var hitpoints: Int = 200,
        var isAlive: Boolean = true
    )

}