package me.theseems.tinybench

import me.theseems.tinybench.exact.ExactGridRecipe
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

class TinyBenchTest {
    @Test
    fun `Make sample grid container then check items success`() {
        val size = 5 x 3
        val grid = grid(size, (0..10).map { size.slot(it) to TestItem(it.toString()) })

        assertEquals(5, grid.height)
        assertEquals(3, grid.width)

        var countCorrect = 0
        for (i in 0 until grid.height) {
            for (j in 0 until grid.width) {
                if (grid.content[i][j] == null && countCorrect < 10) {
                    fail("Count is less than 10")
                }
                if (countCorrect < 10) {
                    assertEquals(countCorrect.toString(), (grid.content[i][j] as TestItem).name)
                }
                countCorrect += 1
            }
        }
    }

    @Test
    fun `Produce exact recipe square single with correct input then check output success`() {
        val size = 50 x 50
        val recipe = ExactGridRecipe(
            name = "sample",
            stackable = true,
            source = grid(size, size.slot(0) to TestItem("A")),
            target = grid(size, size.slot(0) to TestItem("B"))
        )

        val items = recipe.produce(mapOf(size.slot(0) to TestItem("A")))
        assertEquals(1, items.size)
        assertEquals(0, size.slot(items.keys.iterator().next()))
        assertEquals(TestItem("B"), items.values.iterator().next())
        assertNotEquals(TestItem("A"), items.values.iterator().next())
        assertNotEquals(TestItemAnother("B"), items.values.iterator().next())
    }

    @Test
    fun `Produce exact recipe square multiple with correct input then check output success`() {
        val size = 50 x 50
        val recipe = ExactGridRecipe(
            name = "sample",
            stackable = true,
            source = grid(size, size.slot(0) to TestItem("A"), size.slot(1) to TestItem("B")),
            target = grid(size, size.slot(0) to TestItem("C"))
        )

        val items = recipe.produce(mapOf(size.slot(0) to TestItem("A"), size.slot(1) to TestItem("B")))
        assertEquals(1, items.size)
        assertEquals(0, size.slot(items.keys.iterator().next()))
        assertEquals(TestItem("C"), items.values.iterator().next())
    }
}
