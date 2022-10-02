package me.theseems.tinybench

interface RecipeContainer<T : Recipe> {
    val name: String

    fun store(recipe: T)
    fun dispose(recipe: T)

    fun produce(items: ItemMapping, options: RecipeOptions): ItemMapping
}
