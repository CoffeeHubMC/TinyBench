package me.theseems.tinybench

import me.theseems.tinybench.recipe.RecipeManager

val TinyBenchAPI.Companion.recipeManager: RecipeManager
    get() {
        return instance.recipeManager
    }
