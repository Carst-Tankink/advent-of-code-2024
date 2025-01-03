package day23

import util.Solution

class LANParty(fileName: String?) : Solution<Pair<String, String>, Int>(fileName) {
    override fun parse(line: String): Pair<String, String> = line
        .split("-")
        .let { Pair(it[0], it[1]) }

    override fun solve1(data: List<Pair<String, String>>): Int {
        val neighbours = (data + data.map { it.second to it.first })
            .groupBy { it.first }
            .mapValues { it.value.map { p -> p.second } }


        val subGraphs = neighbours.entries.flatMap { (n1, nodes) ->
            nodes.flatMap { n2 ->
                nodes.map { n3 ->
                    listOf(n1, n2, n3).sorted()
                }.filter { it[1] != it[2] }
            }.toSet()
        }

        val cliques = subGraphs.filter { s ->
            s.all { n1 ->
                val others = s - n1
                others.all { n1 in neighbours[it]!! }
            }
        }.toSet()


        return cliques.count { it.any { n -> n.startsWith('t') } }
    }



    override fun solve2(data: List<Pair<String, String>>): Int {
        TODO("Not yet implemented")
    }
}