package me.theseems.tinybench

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import me.theseems.tinybench.item.ItemStackItem
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class SpigotItemTest {
    @Test
    fun `Check recipe with spigot items then check output success`() {
        val itemStackA = ItemStack(Material.ACACIA_BOAT)
        val itemStackB = ItemStack(Material.ACACIA_SAPLING)

        val size = 50 x 50
        val recipe = ExactGridRecipe(
            name = "sample",
            source = grid(size, size.slot(0) to ItemStackItem(itemStackA)),
            target = grid(size, size.slot(0) to ItemStackItem(itemStackB))
        )

        val items = recipe.produce(mapOf(size.slot(0) to ItemStackItem(itemStackA)))
        assertEquals(1, items.size)
        assertEquals(0, size.slot(items.keys.iterator().next()))
        assertEquals(ItemStackItem(itemStackB), items.values.iterator().next())
        assertNotEquals(ItemStackItem(itemStackA), items.values.iterator().next())
    }

    @Test
    fun `Check recipe with spigot items with custom meta then check output success`() {
        val itemStackA = ItemStack(Material.ACACIA_BOAT).also {
            val meta = it.itemMeta
            meta.setDisplayName("a")
            it.itemMeta = meta
        }
        val itemStackB = itemStackA.clone().also {
            val meta = it.itemMeta
            meta.setDisplayName("b")
            it.itemMeta = meta
        }

        assertNotEquals(itemStackA, itemStackB)

        val size = 50 x 50
        val recipe = ExactGridRecipe(
            name = "sample",
            source = grid(size, size.slot(0) to ItemStackItem(itemStackA)),
            target = grid(size, size.slot(0) to ItemStackItem(itemStackB))
        )

        val items = recipe.produce(mapOf(size.slot(0) to ItemStackItem(itemStackA)))
        assertEquals(1, items.size)
        assertEquals(0, size.slot(items.keys.iterator().next()))
        assertEquals(ItemStackItem(itemStackB), items.values.iterator().next())
        assertNotEquals(ItemStackItem(itemStackA), items.values.iterator().next())
    }

    companion object {
        private lateinit var server: ServerMock
        private lateinit var plugin: TinyBench

        @JvmStatic
        @BeforeAll
        fun setUp() {
            server = MockBukkit.mock()
            plugin = MockBukkit.load(TinyBench::class.java)
        }

        @JvmStatic
        @AfterAll
        fun tearDown() {
            MockBukkit.unmock()
        }
    }
}
