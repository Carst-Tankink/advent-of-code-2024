package day23

import util.Solution

class LANParty(fileName: String?) : Solution<Pair<String, String>, String>(fileName) {
    override fun parse(line: String): Pair<String, String> = line
        .split("-")
        .let { Pair(it[0], it[1]) }

    override fun solve1(data: List<Pair<String, String>>): String {
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

        return cliques.count { it.any { n -> n.startsWith('t') } }.toString()
    }

    override fun solve2(data: List<Pair<String, String>>): String {
        val neighbors = data
            .flatMap { listOf(it.first to it.second, it.second to it.first) }
            .groupBy({ it.first }) { it.second }
            .mapValues { it.value.toSet() }

        fun findMaximumClique(
            P: Set<String>,
            R: Set<String> = emptySet(),
            X: Set<String> = emptySet()
        ): Set<String> {
            return if (P.isEmpty() && X.isEmpty()) R else {
                val withMostNeighbors: String = (P + X).maxBy { neighbors.getValue(it).size }
                P.minus(neighbors.getValue(withMostNeighbors)).map { V ->
                    findMaximumClique(
                        P intersect neighbors.getValue(V),
                        R + V,
                        X intersect neighbors.getValue(V)
                    )
                }.maxBy { it.size }
            }
        }
        return findMaximumClique(neighbors.keys).sorted().joinToString(",")
    }


}