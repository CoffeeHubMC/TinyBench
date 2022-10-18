package me.theseems.tinybench.recipe

import me.theseems.tinybench.item.ItemMapping

interface RecipeContainer<T : Recipe> {
    val name: String

    fun store(recipe: T)
    fun dispose(recipe: T)
    fun dispose()

    data class RecipeResult(val items: ItemMapping, val leftovers: ItemMapping, val recipe: Recipe?) {
        companion object {
            fun empty(items: ItemMapping): RecipeResult {
                return RecipeResult(emptyMap(), items, null)
            }
        }
    }

    fun produce(items: ItemMapping, options: RecipeOptions): RecipeResult
}
