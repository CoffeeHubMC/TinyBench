package me.theseems.tinybench.recipe

import me.theseems.tinybench.item.ItemMapping

interface RecipeManager {
    fun <T : Recipe> store(recipeContainer: RecipeContainer<T>)
    fun dispose(name: String)
    fun drop(name: String)
    fun getAll(): Collection<RecipeContainer<*>>
    fun <T : Recipe> get(type: String): RecipeContainer<T>?

    fun produce(items: ItemMapping, options: RecipeOptions): RecipeContainer.RecipeResult
}
