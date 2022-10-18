package me.theseems.tinybench.shapeless

import me.theseems.tinybench.item.Item
import me.theseems.tinybench.item.ItemMapping
import me.theseems.tinybench.recipe.RecipeContainer
import me.theseems.tinybench.recipe.RecipeOptions

class ShapelessRecipeContainer(override val name: String = "shapeless") : RecipeContainer<ShapelessRecipe> {
    private val map: MutableMap<HashSet<Item>, ShapelessRecipe> = mutableMapOf()

    override fun store(recipe: ShapelessRecipe) {
        if (recipe.source in map) {
            throw IllegalStateException(
                "Recipe collision: attempt to add ${recipe.name}, present: ${map[recipe.source]!!.name}"
            )
        }
        map[recipe.source] = recipe
    }

    override fun dispose(recipe: ShapelessRecipe) {
        map.remove(recipe.source)
    }

    override fun dispose() {
        map.clear()
    }

    override fun produce(items: ItemMapping, options: RecipeOptions): RecipeContainer.RecipeResult {
        val recipe = map[HashSet(items.values)]
        if (recipe == null || options.block?.contains(recipe) == true) {
            return RecipeContainer.RecipeResult.empty(items)
        }

        val output = recipe.produce(items)
        return RecipeContainer.RecipeResult(output, emptyMap(), recipe)
    }
}
