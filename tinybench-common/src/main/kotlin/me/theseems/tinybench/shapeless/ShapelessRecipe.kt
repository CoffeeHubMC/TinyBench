package me.theseems.tinybench.shapeless

import me.theseems.tinybench.item.Item
import me.theseems.tinybench.item.ItemMapping
import me.theseems.tinybench.recipe.Recipe
import me.theseems.tinybench.slot
import me.theseems.tinybench.x

open class ShapelessRecipe(
    override val name: String,
    val source: HashSet<Item>,
    private val target: HashSet<Item> = hashSetOf()
) : Recipe {
    override fun produce(items: ItemMapping): ItemMapping {
        val maxX = items.keys.maxBy { it.x }.x
        val maxY = items.keys.maxBy { it.y }.y
        val size = maxX x maxY

        val inputSet = HashSet(items.values)
        if (inputSet == source) {
            return target.mapIndexed { index, item -> size.slot(index) to item }.toMap()
        }
        return emptyMap()
    }
}
