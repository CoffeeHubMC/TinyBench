package me.theseems.tinybench

import me.theseems.tinybench.recipe.Recipe
import me.theseems.tinybench.recipe.RecipeContainer
import me.theseems.tinybench.recipe.RecipeManager

val RecipeManager.exactContainer: RecipeContainer<ExactGridRecipe>
    get() {
        return get("exact")!!
    }

fun <T : Recipe> RecipeManager.register(type: String, recipe: T) =
    (get<T>(type) ?: throw IllegalStateException("Failed to get recipe container type of '$type'")).store(recipe)
