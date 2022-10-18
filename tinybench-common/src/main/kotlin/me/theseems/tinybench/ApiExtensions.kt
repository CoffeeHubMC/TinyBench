package me.theseems.tinybench

import me.theseems.tinybench.item.ItemMapping
import me.theseems.tinybench.recipe.RecipeManager
import me.theseems.tinybench.recipe.RecipeOptions

val TinyBenchAPI.Companion.recipeManager: RecipeManager
    get() {
        return instance.recipeManager
    }

fun ItemMapping.boundingSize(): RecipeOptions.SizeOptions {
    val maxX = keys.maxByOrNull { it.x }?.x ?: 0
    val maxY = keys.maxByOrNull { it.y }?.y ?: 0
    return maxX x maxY
}
