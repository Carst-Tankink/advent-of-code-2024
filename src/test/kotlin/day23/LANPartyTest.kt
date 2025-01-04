package day23

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LANPartyTest {
    private val sample1 = LANParty("/day23/sample1")

    @Test
    fun sample1star1() {
        assertEquals("7", sample1.star1())
    }

    @Test
    fun sample1star2() {
        assertEquals("co,de,ka,ta", sample1.star2())
    }
}