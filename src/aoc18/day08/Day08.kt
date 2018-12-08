object Day08 {

    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        teil1u2()
    }

    class Node(val name: String, val childCount: Int, val metaCount: Int, var value:Int = 0,
            val childNodes:ArrayList<Node> = ArrayList(), var nodeEndPos: Int = 0) {
        override fun toString(): String {
            return "[$name #$childCount #$metaCount]"
        }
    }

    var charSeq = 'A'
    var sum = 0

    @Throws(Exception::class)
    private fun teil1u2() {
        val nodes = ArrayList<Node>()

        this.javaClass.getResourceAsStream("aoc18/day08/input.txt")
            .bufferedReader().forEachLine {
            val input = it.split(" ").map { it -> (it.toInt()) }
            val node = parseRec(input, 0, nodes)
            println("Root value: " + node.value)
        }

        println("Sum: $sum")
    }

    private fun parseRec(input:List<Int>, pos:Int, nodes:ArrayList<Node>):Node {
        val node = Node(charSeq.toString(), input.get(pos + 0), input.get(pos + 1))
        println("Node: $node - pos: $pos")
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
            val metadata = input.get(pos2+meta-1)
            metaValue += metadata
            sum += metadata
        }

        if (node.childCount == 0) {
            node.value = metaValue
        } else {
            for (meta in 1..node.metaCount) {
                val metadata = input.get(pos2+meta-1)
                if (metadata > 0 && metadata <= node.childNodes.size) {
                    node.value += node.childNodes[metadata-1].value
                }
            }
        }
        node.nodeEndPos = pos2 + node.metaCount
        return node
    }

}