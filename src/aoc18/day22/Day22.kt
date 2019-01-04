object Day22 {

    @JvmStatic
    fun main(args: Array<String>) {
        part1()
        part2()
    }

    private val extMap = 50
    private var depth = 0
    private var target = Pos(0, 0)

    private fun part1() {
        parseInput()
        val map = buildMap(target.x + 1, target.y + 1)

        val risk = map.flatten().sumBy { t -> t.type }
        println("Part1: total risk level: $risk")
    }

    private fun part2() {
        parseInput()
        val map = buildMap(target.x + extMap, target.y + extMap)

        val startNode = Node(Pos(0, 0), Tool.Torch)
        val distMap = mutableMapOf(startNode to 0)

        val unvisited = mutableSetOf(startNode)
        val visited = mutableSetOf<Node>()

        while (unvisited.isNotEmpty()) {
            val node = unvisited.minBy { n -> distMap[n]!! }!!
            unvisited.remove(node)
            visited.add(node)

            changeTool(node, visited, unvisited, map, distMap)

            for (nextPos in listOf(
                Pos(node.p.x + 1, node.p.y), Pos(node.p.x - 1, node.p.y),
                Pos(node.p.x, node.p.y + 1), Pos(node.p.x, node.p.y - 1)
            )) {
                changePos(nextPos, node, visited, unvisited, map, distMap)
            }
        }
        println("Part2: ${distMap[Node(target, Tool.Torch)]}")
    }

    private fun changeTool(
        node: Node,
        visited: MutableSet<Node>,
        unvisited: MutableSet<Node>,
        map: Array<Array<Region>>,
        distMap: MutableMap<Node, Int>
    ) {
        val type = map[node.p.x][node.p.y].type
        val nextTool = Tool.values().find { t -> t != node.tool && type != t.incompat }!!

        val nextToolNode = Node(node.p, nextTool)
        if (!visited.contains(nextToolNode)) {
            unvisited.add(nextToolNode)
            val toolDist = 7 + distMap[node]!!
            distMap[nextToolNode] = Math.min(distMap.getOrDefault(nextToolNode, toolDist), toolDist)
        }
    }

    private fun changePos(
        nextPos: Pos,
        node: Node,
        visited: MutableSet<Node>,
        unvisited: MutableSet<Node>,
        map: Array<Array<Region>>,
        distMap: MutableMap<Node, Int>
    ) {
        if (!(nextPos.x >= 0 && nextPos.y >= 0 && nextPos.x < target.x + extMap && nextPos.y < target.y + extMap)) {
            return
        }
        val nextNode = Node(nextPos, node.tool)
        if (visited.contains(nextNode)) {
            return
        }
        val nextType = map[nextNode.p.x][nextNode.p.y].type
        if (nextType == node.tool.incompat) {
            return
        }

        val dist = 1 + distMap[node]!!
        unvisited.add(nextNode)
        distMap[nextNode] = Math.min(distMap.getOrDefault(nextNode, dist), dist)
    }

    private fun buildMap(dimx: Int, dimy: Int): Array<Array<Region>> {
        val map = Array(dimx) { Array(dimy) { Region(0, 0, 0) } }

        for (y in 0..(dimy-1)) {
            for (x in 0..(dimx-1)) {
                if ((x == 0 && y == 0) || (x == target.x && y == target.y)) {
                    map[x][y].geoIndex = 0
                } else if (y == 0) {
                    map[x][y].geoIndex = x * 16807
                } else if (x == 0) {
                    map[x][y].geoIndex = y * 48271
                } else {
                    map[x][y].geoIndex = map[x - 1][y].erLvl * map[x][y - 1].erLvl
                }

                map[x][y].erLvl = (map[x][y].geoIndex + depth) % 20183
                map[x][y].type = map[x][y].erLvl % 3
            }
        }
        return map
    }

    private fun parseInput() {
        this.javaClass.getResourceAsStream("aoc18/day22/input.txt")
            .bufferedReader().forEachLine { line ->
                if (line.startsWith("depth: ")) {
                    depth = line.substring("depth: ".length).toInt()
                } else
                    if (line.startsWith("target: ")) {
                        target = Pos(
                            line.substring("target: ".length, line.indexOf(',')).toInt(),
                            line.substring(line.indexOf(',') + 1).toInt()
                        )
                    }
            }
    }

    data class Pos(val x: Int, val y: Int)

    data class Region(var geoIndex: Int, var erLvl: Int, var type: Int)

    data class Node(val p: Pos, val tool: Tool)

    enum class Tool(val incompat: Int) {
        ClimbingGear(2), Torch(1), Neither(0)
    }
}