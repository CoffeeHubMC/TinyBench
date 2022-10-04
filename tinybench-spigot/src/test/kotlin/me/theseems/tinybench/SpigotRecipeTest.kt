package me.theseems.tinybench

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import me.theseems.tinybench.SimpleRecipeOptions.Companion.allowingOnlyRecipes
import me.theseems.tinybench.item.ItemStackItem.Companion.itemOf
import org.bukkit.Material
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class SpigotRecipeTest {

    @Test
    fun `test smth i dunno`() {
        val size = 3 x 3
        TinyBenchAPI.recipeManager.exactContainer.store(
            ExactGridRecipe(
                name = "test",
                stackable = true,
                source = grid(
                    size,
                    size.slot(0) to itemOf(Material.ACACIA_SAPLING),
                    size.slot(1) to itemOf(Material.ACACIA_SAPLING),
                    size.slot(2) to itemOf(Material.ACACIA_SAPLING)
                ),
                target = grid(1 x 1, Slot.zero to itemOf(Material.DIAMOND))
            )
        )

        val produced = TinyBenchAPI.recipeManager.produce(
            mapOf(
                size.slot(0) to itemOf(Material.ACACIA_SAPLING),
                size.slot(1) to itemOf(Material.ACACIA_SAPLING),
                size.slot(2) to itemOf(Material.ACACIA_SAPLING)
            ),
            3 x 3 allowingOnlyRecipes listOf("test")
        )

        println(produced)
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
