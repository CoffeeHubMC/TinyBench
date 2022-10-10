package me.theseems.tinybench

import me.theseems.tinybench.item.ItemMapping
import me.theseems.tinybench.recipe.Recipe
import me.theseems.tinybench.recipe.RecipeContainer
import me.theseems.tinybench.recipe.RecipeManager
import me.theseems.tinybench.recipe.RecipeOptions

class SimpleRecipeManager : RecipeManager {
    private val containerMap: MutableMap<String, RecipeContainer<*>> = mutableMapOf()
    private val recipeMap: MutableMap<String, Recipe> = mutableMapOf()

    override fun <T : Recipe> store(recipeContainer: RecipeContainer<T>) {
        containerMap[recipeContainer.name] = recipeContainer
    }

    override fun dispose(name: String) {
        val container = containerMap[name] ?: return
        container.dispose()
    }

    override fun drop(name: String) {
        val container = containerMap[name] ?: return
        container.dispose()
        containerMap.remove(name)
    }

    override fun getAll(): Collection<RecipeContainer<*>> {
        return containerMap.values
    }

    override fun <T : Recipe> get(type: String): RecipeContainer<T>? {
        @Suppress("UNCHECKED_CAST")
        return containerMap[type] as? RecipeContainer<T>?
    }

    override fun getRecipe(name: String) = recipeMap[name]

    override fun getAllRecipes() = recipeMap.values

    override fun storeRecipe(recipe: Recipe) {
        if (recipe.name in recipeMap) {
            throw IllegalStateException("Recipe '${recipe.name}' is already registered")
        }
        recipeMap[recipe.name] = recipe
    }

    override fun disposeRecipe(recipeName: String) {
        recipeMap.remove(recipeName)
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
