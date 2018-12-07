import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Main {

    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        teil1()
        teil2()
    }

    class Node(val name: String, var duration: Int = 0, val toNodes: ArrayList<String> = ArrayList(), val fromNodes: ArrayList<String> = ArrayList()) {
        override fun toString(): String {
            return name
        }
    }

    @Throws(Exception::class)
    private fun teil2() {
        val durationOffset = 61
        val workerCount = 5

        val nodes = ArrayList<Node>()

        //val file = File("day07-test.txt")
        val file = File("day07-input.txt")
        file.forEachLine {
            val nodeFrom = it[5].toString()
            val nodeTo = it[36].toString()

            var nodesFrom = nodes.find { node ->  node.name.equals(nodeFrom) }
            if (nodesFrom == null) {
                nodesFrom = Node(nodeFrom, nodeFrom[0].toInt() - 'A'.toInt() + durationOffset)
                nodes.add(nodesFrom)
            }
            nodesFrom.toNodes.add(nodeTo)

            var nodesTo = nodes.find { node ->  node.name.equals(nodeTo) }
            if (nodesTo == null) {
                nodesTo = Node(nodeTo, nodeTo[0].toInt() - 'A'.toInt() + durationOffset)
                nodes.add(nodesTo)
            }
            nodesTo.fromNodes.add(nodeFrom)
        }

        val openNodes: ArrayList<Node> = ArrayList()
        openNodes.addAll(nodes.filter { n1 -> (nodes.find { n2 -> (n2.toNodes.contains(n1.name)) }) == null })

        println("Start nodes: $openNodes")
        print("Result: ")

        val workers = IntArray(workerCount, { 0 })
        val workerNodes = Array<Node?>(workerCount, { null })
        var currSec = 0
        val doneNodes = ArrayList<String>()
        while (doneNodes.size < nodes.size || workerNodes.find { n ->  (n != null)} != null) {
            println("sec: $currSec")
            for (workerEndIdx in  workers.indices.filter { wIdx -> (workers[wIdx] == currSec) }) {
                if (workerNodes[workerEndIdx] != null) {
                    println("worker end  : $workerEndIdx --> ${workerNodes[workerEndIdx]!!.name}")
                    doneNodes.add(workerNodes[workerEndIdx]!!.name)
                    openNodes.addAll(
                            workerNodes[workerEndIdx]!!.toNodes
                                    .map { toNodeName -> (nodes.find { n -> (n.name.equals(toNodeName)) })!! }
                                    .filter { n -> (!openNodes.contains(n)) })
                    workerNodes[workerEndIdx] = null
                }
            }

            for (workerIdx in workers.indices.filter { wIdx -> (workers[wIdx] <= currSec) }) {
                openNodes.sortBy { it.name }

                val nextNode = openNodes.find { on -> (on.fromNodes.subtract(doneNodes).isEmpty()) }
                if (nextNode != null) {
                    openNodes.remove(nextNode)

                    println("worker start: $workerIdx ==> ${nextNode.name}")

                    workerNodes[workerIdx] = nextNode
                    workers[workerIdx] = currSec + nextNode.duration
                }
            }
            currSec++
        }
        println("Duration: ${currSec-1}")
    }

    @Throws(Exception::class)
    private fun teil1() {

        val nodes = ArrayList<Node>()

        //val file = File("day07-test.txt")
        val file = File("day07-input.txt")
        file.forEachLine {
            val nodeFrom = it[5].toString()
            val nodeTo = it[36].toString()

            var nodesFrom = nodes.find { node ->  node.name.equals(nodeFrom) }
            if (nodesFrom == null) {
                nodesFrom = Node(nodeFrom)
                nodes.add(nodesFrom)
            }
            nodesFrom.toNodes.add(nodeTo)

            var nodesTo = nodes.find { node ->  node.name.equals(nodeTo) }
            if (nodesTo == null) {
                nodesTo = Node(nodeTo)
                nodes.add(nodesTo)
            }
            nodesTo.fromNodes.add(nodeFrom)
        }

        var openNodes: ArrayList<Node> = ArrayList()
        openNodes.addAll(nodes.filter { n1 -> (nodes.find { n2 -> (n2.toNodes.contains(n1.name)) }) == null })

        println("Start nodes: $openNodes")
        print("Result: ")
        var doneNodes = ArrayList<String>()
        while (!openNodes.isEmpty()) {
            openNodes.sortBy { it.name }

            val nextNode = openNodes.find { on -> (on.fromNodes.subtract(doneNodes).isEmpty()) }
            openNodes.remove(nextNode)
            doneNodes.add(nextNode!!.name)

            print("${nextNode.name}")

            openNodes.addAll(
                    nextNode.toNodes
                            .map { toNodeName -> (nodes.find { n -> (n.name.equals(toNodeName)) })!! }
                            .filter { n -> (!openNodes.contains(n)) })
        }
        println()
    }

}