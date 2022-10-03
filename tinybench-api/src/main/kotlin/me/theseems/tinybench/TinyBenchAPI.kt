package me.theseems.tinybench

import me.theseems.tinybench.recipe.RecipeManager

class TinyBenchAPI private constructor(val recipeManager: RecipeManager) {
    companion object {
        @Volatile
        lateinit var instance: TinyBenchAPI

        fun initialize(recipeManager: RecipeManager) {
            if (!::instance.isInitialized) {
                instance = TinyBenchAPI(recipeManager)
            }
        }
    }
}
