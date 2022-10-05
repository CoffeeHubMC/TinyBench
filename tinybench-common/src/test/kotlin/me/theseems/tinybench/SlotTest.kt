package me.theseems.tinybench

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SlotTest {
    @Test
    fun checkArithmetics() {
        for (size in listOf(1 x 9, 2 x 9, 3 x 9, 4 x 9, 5 x 9, 6 x 9)) {
            for (i in 0 until size.height) {
                for (j in 0 until size.width) {
                    assertEquals(Slot(i, j), size.slot(i * size.width + j))
                }
            }
        }
    }
}
