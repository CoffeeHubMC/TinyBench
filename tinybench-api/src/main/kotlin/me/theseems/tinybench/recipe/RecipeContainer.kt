package me.theseems.tinybench.recipe

import me.theseems.tinybench.item.ItemMapping

interface RecipeContainer<T : Recipe> {
    val name: String

    fun store(recipe: T)
    fun dispose(recipe: T)
    fun dispose()

    data class RecipeResult(val items: ItemMapping, val leftovers: ItemMapping, val recipe: Recipe)

    fun produce(items: ItemMapping, options: RecipeOptions): RecipeResult
}
