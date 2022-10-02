package me.theseems.tinybench

class ExactGridRecipeContainer : RecipeContainer<ExactGridRecipe> {
    override val name: String = "exact"
    private val map: MutableMap<GridContainer, Recipe> = mutableMapOf()

    override fun produce(items: ItemMapping, options: RecipeOptions): ItemMapping {
        val recipe = map[items.toGridContainer(options.size)]
        if (recipe == null || options.block?.contains(recipe) == true) {
            return emptyMap()
        }

        return recipe.produce(items)
    }

    override fun dispose(recipe: ExactGridRecipe) {
        map.remove(recipe.source)
    }

    override fun store(recipe: ExactGridRecipe) {
        if (recipe.source in map) {
            throw IllegalStateException(
                "Multiple recipes type of 'exact' present: '${recipe.name}' and '${map[recipe.source]!!.name}'"
            )
        }
        map[recipe.source] = recipe
    }
}
