package me.theseems.tinybench.recipes

import me.theseems.tinybench.config.RecipeConfig
import me.theseems.tinybench.recipe.Recipe
import java.util.logging.Logger

abstract class RecipeParser<T : Recipe> {
    abstract fun makeRecipe(name: String, config: RecipeConfig, logger: Logger): T
}
