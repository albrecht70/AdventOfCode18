import java.io.File

object Day08 {

    @JvmStatic
    fun main(args: Array<String>) {
        part1and2()
    }

    private fun part1and2() {
        val input = parseInput()
        val nodes = mutableListOf<Node>()
        val node = parseRec(input, 0, nodes)

        println("Part1: sum: $sum")
        println("Part2: root value: ${node.value}")
    }

    private var charSeq = 'A'
    private var sum = 0

    private fun parseRec(input: List<Int>, pos: Int, nodes: MutableList<Node>): Node {
        val node = Node(input[pos + 0], input[pos + 1])
        //println("Node: $node - pos: $pos")
        nodes.add(node)
        charSeq = charSeq.inc()

        var pos2 = pos + 2
        for (child in 1..node.childCount) {
            val childNode = parseRec(input, pos2, nodes)
            pos2 = childNode.nodeEndPos
            node.childNodes.add(childNode)
        }
        var metaValue = 0
        for (meta in 1..node.metaCount) {
            val metadata = input[pos2 + meta - 1]
            metaValue += metadata
            sum += metadata
        }

        if (node.childCount == 0) {
            node.value = metaValue
        } else {
            for (meta in 1..node.metaCount) {
                val metadata = input[pos2 + meta - 1]
                if (metadata > 0 && metadata <= node.childNodes.size) {
                    node.value += node.childNodes[metadata - 1].value
                }
            }
        }
        node.nodeEndPos = pos2 + node.metaCount
        return node
    }

    private fun parseInput(): List<Int> =
        File(this.javaClass.getResource("aoc18/day08/input.txt").toURI())
            .readText().split(" ").map { text -> (text.toInt()) }

    data class Node(
        val childCount: Int, val metaCount: Int, var value: Int = 0,
        val childNodes: ArrayList<Node> = ArrayList(), var nodeEndPos: Int = 0
    )

}