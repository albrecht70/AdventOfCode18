import kotlin.math.max

object Day24 {

    @JvmStatic
    fun main(args: Array<String>) {
        teil1()
        teil2()
    }

    val immuneArmy = mutableListOf<Group>()
    val infectionArmy = mutableListOf<Group>()

    private fun teil2() {

        var boost = 0
        do {
            parseInput(boost)

            while (immuneArmy.sumBy { a -> a.units } > 0 && infectionArmy.sumBy { a -> a.units } > 0) {

                val attacks = mutableMapOf<Group,Attack>()
                findOpponents(immuneArmy, infectionArmy, attacks)
                findOpponents(infectionArmy, immuneArmy, attacks)
                doAttack(attacks)
            }
            println("Boost: $boost - Immune System: ${immuneArmy.sumBy { a -> a.units }} - Infection: ${infectionArmy.sumBy { a -> a.units }}")
            boost++
        } while (immuneArmy.sumBy { a -> a.units } == 0)
    }

    private fun teil1() {
        parseInput()

        while (immuneArmy.sumBy { a -> a.units } > 0 && infectionArmy.sumBy { a -> a.units } > 0) {

            val attacks = mutableMapOf<Group,Attack>()
            findOpponents(immuneArmy, infectionArmy, attacks)
            findOpponents(infectionArmy, immuneArmy, attacks)
            doAttack(attacks)

            println("Immune System: ${immuneArmy.sumBy { a -> a.units }} - Infection: ${infectionArmy.sumBy { a -> a.units }}")
        }
    }

    private fun doAttack(attacks: MutableMap<Group, Attack>) {
        attacks.entries.sortedByDescending { e -> e.key.initiative }.forEach { e ->
            val attacker = e.key
            val attack = Attack(e.key, e.value.opponent)
            if (attacker.hitPoints > 0) {
                //println("Attack: $attacker ==[${attack.damage} / ${attack.opponent.units} /${attack.damage} / ${attack.opponent.hitPoints}]==> ${attack.opponent}")
                attack.opponent.units = max(attack.opponent.units - (attack.damage / attack.opponent.hitPoints), 0)
            }
        }
    }

    private fun findOpponents(attacker: MutableList<Group>, opponents: MutableList<Group>, attacks: MutableMap<Group,Attack>) {
        val groups = opponents.filter { it.units > 0 }.toMutableList()

        attacker.filter { it.units > 0 }.sortedByDescending { it.units * it.attackPower}.forEach { g ->
            val bestOp: Attack? = groups.map { op -> Attack(g, op) }.max()
            if (bestOp != null) {
                attacks[g] = bestOp
                groups.remove(bestOp.opponent)
            }
        }
    }

    val unitRegex = "^([0-9]*) units".toRegex()
    val hitRegex = " ([0-9]*) hit points".toRegex()
    val immuneToRegex = "immune to ([a-z]+)[, ]*([a-z]*)[, ]*([a-z]*)".toRegex()
    val weakToRegex = "weak to ([a-z]+)[, ]*([a-z]*)[, ]*([a-z]*)".toRegex()
    val attackRegex = " ([0-9]*) ([a-z]*) damage".toRegex()
    val initiativeRegex = "initiative ([0-9]*)".toRegex()

    private fun parseInput(boost: Int = 0) {
        immuneArmy.clear()
        infectionArmy.clear()
        var isImmuneSystem = true
        this.javaClass.getResourceAsStream("aoc18/day24/test.txt")
            .bufferedReader().forEachLine { line ->
                if (line.startsWith("Immune System")) {
                    isImmuneSystem = true
                } else if (line.startsWith("Infection")) {
                    isImmuneSystem = false
                } else if (line.indexOf(" unit") != -1) {
                    val units = unitRegex.find(line)!!.groups[1]!!.value.toInt()
                    val hitPoints = hitRegex.find(line)!!.groups[1]!!.value.toInt()

                    val weaks = mutableListOf<CombatType>()
                    if (weakToRegex.containsMatchIn(line)) {
                        weaks.addAll(weakToRegex.find(line)!!.groupValues.filter { str ->
                            !str.startsWith("weak") && str.trim().isNotEmpty()
                        }.map { str -> CombatType.valueOf(str) })
                    }

                    val immunes = mutableListOf<CombatType>()
                    if (immuneToRegex.containsMatchIn(line)) {
                        immunes.addAll(immuneToRegex.find(line)!!.groupValues.filter { str ->
                            !str.startsWith("immune") && str.trim().isNotEmpty()
                        }.map { str -> CombatType.valueOf(str) })
                    }

                    var attackPower = attackRegex.find(line)!!.groups[1]!!.value.toInt()
                    val attackType = CombatType.valueOf(attackRegex.find(line)!!.groups[2]!!.value)
                    val initiative = initiativeRegex.find(line)!!.groups[1]!!.value.toInt()

                    attackPower = if (isImmuneSystem) attackPower+boost else attackPower

                    val system = Group(units, hitPoints, weaks, immunes,
                        attackPower, attackType, initiative)

                    if (isImmuneSystem) {
                        immuneArmy.add(system)
                    } else {
                        infectionArmy.add(system)
                    }
                }

            }
    }

    data class Attack(val attacker: Group, val opponent: Group): Comparable<Attack> {
        private val effPower: Int = attacker.attackPower * attacker.units
        private val oppEffPower: Int = opponent.attackPower * opponent.units
        private val initiative: Int = opponent.initiative
        val damage: Int

        init {
            this.damage = when {
                opponent.immune.contains(attacker.attackType) -> 0
                opponent.weakness.contains(attacker.attackType) -> this.effPower * 2
                else -> this.effPower
            }
        }

        override operator fun compareTo(a: Attack): Int = when {
            damage > a.damage -> 1
            damage == a.damage && oppEffPower > a.oppEffPower -> 1
            damage == a.damage && oppEffPower == a.oppEffPower && initiative > a.initiative -> 1
            damage == a.damage && oppEffPower == a.oppEffPower && initiative == a.initiative -> 0
            else -> -1
        }
    }

    data class Group(var units: Int, var hitPoints: Int,
                     val weakness: List<CombatType>, val immune: List<CombatType>,
                     val attackPower: Int, val attackType: CombatType,
                     val initiative: Int)

    enum class CombatType {
        radiation, bludgeoning, fire, cold, slashing
    }
}