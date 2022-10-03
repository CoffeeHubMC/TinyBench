package me.theseems.tinybench

import me.theseems.tinybench.item.ItemMapping
import me.theseems.tinybench.recipe.Recipe

class ExactGridRecipe(
    override val name: String,
    val source: GridContainer,
    val target: GridContainer
) : Recipe {
    override fun produce(items: ItemMapping): ItemMapping {
        if (items.toGridContainer(source.height, source.width) != source) {
            return emptyMap()
        }
        return target.toMapping()
    }
}
