package me.theseems.tinybench

import me.theseems.tinybench.item.Item
import me.theseems.tinybench.item.ItemMapping
import me.theseems.tinybench.recipe.RecipeContainer
import me.theseems.tinybench.recipe.RecipeOptions
import kotlin.math.min

class ExactGridRecipeContainer : RecipeContainer<ExactGridRecipe> {
    override val name: String = "exact"
    private val map: MutableMap<GridContainer, Pair<Map<Slot, Int>, ExactGridRecipe>> = mutableMapOf()

    override fun produce(items: ItemMapping, options: RecipeOptions): RecipeContainer.RecipeResult {
        val emptyResult = RecipeContainer.RecipeResult(emptyMap(), items)

        val singularGrid = items.toSingularGridContainer(options.size)
        val (amounts, recipe) = map[singularGrid] ?: return emptyResult
        if (options.block?.contains(recipe) == true) {
            return emptyResult
        }

        var times: Int? = null
        for ((slot, amount) in amounts) {
            if (slot !in items) {
                return emptyResult
            }
            if (items[slot]!!.amount < amount) {
                return emptyResult
            }
            times = min(times ?: Int.MAX_VALUE, items[slot]!!.amount / amount)
        }

        times = times ?: 1

        val result: MutableMap<Slot, Item> = mutableMapOf()
        if (times > 0) {
            val produced = recipe.target.toMapping()
            for (key in produced.keys) {
                val producedItem = produced[key]?.clone()!!
                producedItem.amount *= times

                result[key] = producedItem
            }
        }

        val leftovers: MutableMap<Slot, Item> = mutableMapOf()
        for ((slot, item) in items) {
            if (item.amount == times) {
                continue
            }

            val leftItem = item.clone()
            leftItem.amount -= times
            leftovers[slot] = leftItem
        }

        return RecipeContainer.RecipeResult(result, leftovers)
    }

    override fun dispose(recipe: ExactGridRecipe) {
        map.remove(recipe.source)
    }

    override fun store(recipe: ExactGridRecipe) {
        if (recipe.source in map) {
            throw IllegalStateException(
                "Multiple recipes type of 'exact' present: '${recipe.name}' and '${map[recipe.source]!!.second.name}'"
            )
        }

        val (grid, amounts) = recipe.source.stripAmount()
        map[grid] = Pair(amounts, recipe)
    }
}
