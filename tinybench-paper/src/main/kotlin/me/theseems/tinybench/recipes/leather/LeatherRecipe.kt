package me.theseems.tinybench.recipes.leather

import me.theseems.tinybench.item.Item
import me.theseems.tinybench.item.ItemMapping
import me.theseems.tinybench.recipe.Recipe

class LeatherRecipe(override val name: String, val additional: Set<Item>) : Recipe {
    override fun produce(items: ItemMapping): ItemMapping {
        return emptyMap()
    }
}
