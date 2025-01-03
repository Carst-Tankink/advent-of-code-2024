package day23

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LANPartyTest {
    private val sample1 = LANParty("/day23/sample1")

    @Test
    fun sample1star1() {
        assertEquals(7, sample1.star1())
    }
}