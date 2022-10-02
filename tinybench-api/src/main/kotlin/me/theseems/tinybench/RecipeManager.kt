package me.theseems.tinybench

interface RecipeManager {
    fun <T : Recipe> store(recipeContainer: RecipeContainer<T>)
    fun <T : Recipe> get(type: String): RecipeContainer<T>?

    fun produce(items: ItemMapping, options: RecipeOptions): ItemMapping
}
