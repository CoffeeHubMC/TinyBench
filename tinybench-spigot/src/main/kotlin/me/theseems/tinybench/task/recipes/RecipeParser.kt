package me.theseems.tinybench.task.recipes

import me.theseems.tinybench.recipe.Recipe
import java.util.logging.Logger
import me.theseems.tinybench.config.RecipeConfig

abstract class RecipeParser<T : Recipe> {
    abstract fun makeRecipe(name: String, config: RecipeConfig, logger: Logger): T
}
