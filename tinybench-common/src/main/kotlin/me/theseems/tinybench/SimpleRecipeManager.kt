package me.theseems.tinybench

import me.theseems.tinybench.item.ItemMapping
import me.theseems.tinybench.recipe.Recipe
import me.theseems.tinybench.recipe.RecipeContainer
import me.theseems.tinybench.recipe.RecipeManager
import me.theseems.tinybench.recipe.RecipeOptions

class SimpleRecipeManager : RecipeManager {
    private val containerMap: MutableMap<String, RecipeContainer<*>> = mutableMapOf()

    override fun <T : Recipe> store(recipeContainer: RecipeContainer<T>) {
        containerMap[recipeContainer.name] = recipeContainer
    }

    override fun <T : Recipe> get(type: String): RecipeContainer<T>? {
        @Suppress("UNCHECKED_CAST")
        return containerMap[type] as? RecipeContainer<T>?
    }

    override fun produce(items: ItemMapping, options: RecipeOptions): RecipeContainer.RecipeResult {
        containerMap.values.forEach {
            val (produced, leftovers) = it.produce(items, options)
            if (produced.isNotEmpty()) {
                return RecipeContainer.RecipeResult(produced, leftovers)
            }
        }
        return RecipeContainer.RecipeResult(emptyMap(), items)
    }
}
