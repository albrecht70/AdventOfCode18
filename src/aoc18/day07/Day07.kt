object Day07 {

    @JvmStatic
    fun main(args: Array<String>) {
        part1()
        part2()
    }

    private fun part1() {
        val nodes = parseInput()
        val openNodes =
            nodes.filter { n1 -> (nodes.find { n2 -> (n2.toNodes.contains(n1.name)) }) == null }.toMutableList()

        print("Part1: ")
        val doneNodes = mutableListOf<Char>()
        while (!openNodes.isEmpty()) {
            openNodes.sortBy { it.name }

            val nextNode = openNodes.find { on -> (on.fromNodes.subtract(doneNodes).isEmpty()) }!!
            openNodes.remove(nextNode)
            doneNodes.add(nextNode.name)

            print(nextNode.name)

            openNodes.addAll(
                nextNode.toNodes
                    .map { toNodeName -> (nodes.find { n -> (n.name == toNodeName) })!! }
                    .filter { n -> (!openNodes.contains(n)) })
        }
        println()
    }

    private fun part2() {
        val workerCount = 5

        val nodes = parseInput()
        val openNodes =
            nodes.filter { n1 -> (nodes.find { n2 -> (n2.toNodes.contains(n1.name)) }) == null }.toMutableList()

        print("Result: ")
        val workers = IntArray(workerCount) { 0 }
        val workerNodes = Array<Node?>(workerCount) { null }
        var currSec = 0
        val doneNodes = mutableListOf<Char>()
        while (doneNodes.size < nodes.size || workerNodes.any { it != null }) {

            for (workerEndIdx in workers.indices.filter { wIdx -> (workers[wIdx] == currSec) }) {
                if (workerNodes[workerEndIdx] != null) {
                    //println("worker end  : $workerEndIdx --> ${workerNodes[workerEndIdx]!!.name}")
                    doneNodes.add(workerNodes[workerEndIdx]!!.name)
                    openNodes.addAll(
                        workerNodes[workerEndIdx]!!.toNodes
                            .map { toNodeName -> (nodes.find { n -> (n.name == toNodeName) })!! }
                            .filter { n -> (!openNodes.contains(n)) })
                    workerNodes[workerEndIdx] = null
                }
            }

            for (workerIdx in workers.indices.filter { wIdx -> (workers[wIdx] <= currSec) }) {
                openNodes.sortBy { it.name }
                val nextNode = openNodes.find { on -> (on.fromNodes.subtract(doneNodes).isEmpty()) }
                if (nextNode != null) {
                    openNodes.remove(nextNode)
                    //println("worker start: $workerIdx ==> ${nextNode.name}")
                    workerNodes[workerIdx] = nextNode
                    workers[workerIdx] = currSec + nextNode.duration
                }
            }
            currSec++
        }
        println("Part2: duration: ${currSec - 1}")
    }

    private fun parseInput(): List<Node> {
        val nodes = mutableListOf<Node>()

        this.javaClass.getResourceAsStream("aoc18/day07/input.txt")
            .bufferedReader().forEachLine {
                val nodeFrom = it[5]
                val nodeTo = it[36]

                var nodesFrom = nodes.find { node -> node.name == nodeFrom }
                if (nodesFrom == null) {
                    nodesFrom = Node(nodeFrom)
                    nodes.add(nodesFrom)
                }
                nodesFrom.toNodes.add(nodeTo)

                var nodesTo = nodes.find { node -> node.name == nodeTo }
                if (nodesTo == null) {
                    nodesTo = Node(nodeTo)
                    nodes.add(nodesTo)
                }
                nodesTo.fromNodes.add(nodeFrom)
            }
        return nodes
    }

    data class Node(
        val name: Char,
        val toNodes: MutableList<Char> = mutableListOf(),
        val fromNodes: MutableList<Char> = mutableListOf()
    ) {
        val duration = name.toInt() - 'A'.toInt() + 61
    }
}