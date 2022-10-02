package me.theseems.tinybench

class SimpleRecipeManager : RecipeManager {
    private val containerMap: MutableMap<String, RecipeContainer<*>> = mutableMapOf()

    override fun <T : Recipe> store(recipeContainer: RecipeContainer<T>) {
        containerMap[recipeContainer.name] = recipeContainer
    }

    override fun <T : Recipe> get(type: String): RecipeContainer<T>? {
        @Suppress("UNCHECKED_CAST")
        return containerMap[type] as? RecipeContainer<T>?
    }

    override fun produce(items: ItemMapping, options: RecipeOptions): ItemMapping {
        containerMap.values.forEach {
            val produced = it.produce(items, options)
            if (produced.isNotEmpty()) {
                return produced
            }
        }
        return emptyMap()
    }
}
