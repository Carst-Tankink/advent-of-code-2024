package day2

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RedNosedReportsTest {
    val sample1 = RedNosedReports("/day2/sample1")
    @Test
    fun sample1star1() {
        assertEquals(2, sample1.star1())
    }
}