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

    class TimeEvent(val timestamp: LocalDateTime, val guard: String?, val wakeUp: Boolean, val sleep:Boolean)

    class SleepEvent(val guard: String, val start: LocalDateTime, val end : LocalDateTime)

    @Throws(Exception::class)
    private fun teil2() {
        var sleepEvents = getSleepEvents()

        var maxMin = 0
        var mul1 = 1
        var mul2 = 1

        for (guardEvents in sleepEvents.groupBy { it -> it.guard }) {
            val gEv = ArrayList<SleepEvent>(guardEvents.value)
            gEv.sortBy { ev -> ev.start.minute }
            var minutes = IntArray(60)
            for (ev in gEv) {
                for (minute in ev.start.minute..(ev.end.minute-1)) {
                    minutes[minute]++
                }
            }
            val maxPos = minutes.indices.maxBy { minutes[it] } ?: 1
            if (maxMin < minutes[maxPos]) {
                mul1 = guardEvents.key.substring(1).toInt()
                mul2 = maxPos
                maxMin = minutes[maxPos]
            }
        }

        val result = mul1 * mul2
        println("Result 2: $mul1 * $mul2 = $result")
    }

    @Throws(Exception::class)
    private fun teil1() {
        var sleepEvents = getSleepEvents()

        val maxSleepTime: Pair<String, Int>? = sleepEvents.groupBy { it -> it.guard }
                .entries.map { entry ->
            Pair(entry.key, entry.value.sumBy { (it.end.minute - it.start.minute) })
        }.maxBy { it -> it.second }

        println("max sleep time: $maxSleepTime")

        val mul1 = maxSleepTime!!.first.substring(1).toInt()

        val guardEvents: ArrayList<SleepEvent> = ArrayList(sleepEvents.groupBy { it -> it.guard }.get(maxSleepTime.first)!!)
        guardEvents.sortBy { ev -> ev.start.minute }

        var minutes = IntArray(60)
        for (ev in guardEvents) {
            for (minute in ev.start.minute..(ev.end.minute-1)) {
                minutes[minute]++
            }
        }
        val maxPos = minutes.indices.maxBy { minutes[it] } ?: 1
        val mul2 = maxPos
        val result = mul1 * mul2

        println("Result 1: $mul1 * $mul2 = $result")
    }

    private fun getSleepEvents(): ArrayList<SleepEvent> {
        var events = ArrayList<TimeEvent>()

        //val file = File("day04-test.txt")
        val file = File("day04-input.txt")
        file.forEachLine {
            val timestamp = LocalDateTime.parse(
                    it.substring(1, it.indexOf(']')),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            val guard: String? = Regex("#([0-9])+").find(it)?.value
            val wakeUp = it.contains("wakes up")
            val sleep = it.contains("falls asleep")
            //println("$it -> $timestamp / $sleep / $wakeUp / #$guard")
            events.add(TimeEvent(timestamp, guard, wakeUp, sleep))
        }

        events.sortBy { event -> event.timestamp }

        var sleepEvents = ArrayList<SleepEvent>()
        var currGuard: String? = null
        var currStart: LocalDateTime? = null
        var currEnd: LocalDateTime? = null
        for (event in events) {
            if (currGuard != null && currStart != null && currEnd != null) {
                println("Guard $currGuard \t\t $currStart -> $currEnd")
                sleepEvents.add(SleepEvent(currGuard, currStart, currEnd))

                currStart = null
                currEnd = null
            }
            if (event.guard != null) {
                currGuard = event.guard
                currStart = null
                currEnd = null
            }
            if (event.sleep) {
                currStart = event.timestamp
            } else if (event.wakeUp) {
                currEnd = event.timestamp
            }
        }
        if (currGuard != null && currStart != null && currEnd != null) {
            println("Guard $currGuard \t\t $currStart -> $currEnd")
            sleepEvents.add(SleepEvent(currGuard, currStart, currEnd))
        }
        return sleepEvents
    }

}
