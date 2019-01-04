import java.util.*

object Day20 {

    @JvmStatic
    fun main(args: Array<String>) {
        part1and2()
    }

    private fun part1and2() {
        val charSeq = parseInput().asIterable().iterator()
        val map = mutableSetOf<Pair<Pos,Pos>>()
        createMap(charSeq, map, Pos(0, 0))
        findPath(Pos(0,0), map)
    }

    private fun createMap(charSeq: Iterator<Char>, map: MutableSet<Pair<Pos,Pos>>, pos: Pos):Pos {
        var currPos = pos
        while (charSeq.hasNext()) {
            when (charSeq.next()) {
                'E' -> currPos = move(currPos, Pos(currPos.x+1, currPos.y), map)
                'N' -> currPos = move(currPos, Pos(currPos.x, currPos.y-1), map)
                'W' -> currPos = move(currPos, Pos(currPos.x-1, currPos.y), map)
                'S' -> currPos = move(currPos, Pos(currPos.x, currPos.y+1), map)
                '(' -> currPos = createMap(charSeq, map, currPos)
                '|' -> currPos = pos
                ')' -> return currPos
            }
        }
        return currPos
    }

    private fun move(pos: Pos, nextPos: Pos, map: MutableSet<Pair<Pos,Pos>>): Pos {
        map.add(Pair(pos, nextPos))
        map.add(Pair(nextPos, pos))
        return nextPos
    }

    private fun findPath(startPos: Pos, map: MutableSet<Pair<Pos,Pos>>) {
        val distances = mutableMapOf(startPos to 0)
        val queue = LinkedList<Pos>()

        queue.add(startPos)
        while (queue.isNotEmpty()) {
            val p = queue.remove()
            listOf(
                Pos(p.x, p.y - 1),
                Pos(p.x - 1, p.y),
                Pos(p.x + 1, p.y),
                Pos(p.x, p.y + 1)
            ).filter { np -> !distances.containsKey(np) && map.contains(Pair(p, np)) }
                .forEach { np ->
                    distances[np] = 1 + distances[p]!!
                    queue.add(np)
                }
        }
        val max = distances.maxBy { e -> e.value }!!
        println("Part1: max distance: ${max.value}")
        val largePaths = distances.filterValues { it >= 1000 }.count()
        println("Part2: large paths: $largePaths")
    }

    private fun parseInput(): String {
        val text = this.javaClass.getResourceAsStream("aoc18/day20/input.txt")
            .bufferedReader().readText()
        return text.substring(1, text.length-1)
    }

    data class Pos(val x:Int, val y:Int)
}