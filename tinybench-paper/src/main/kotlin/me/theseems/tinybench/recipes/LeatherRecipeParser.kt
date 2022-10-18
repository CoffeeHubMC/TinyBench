package me.theseems.tinybench.recipes

import me.theseems.tinybench.config.RecipeConfig
import me.theseems.tinybench.item.parser.ItemFactory
import me.theseems.tinybench.recipes.leather.LeatherRecipe
import java.util.logging.Logger

class LeatherRecipeParser : RecipeParser<LeatherRecipe>() {
    override fun makeRecipe(name: String, config: RecipeConfig, logger: Logger): LeatherRecipe {
        val factory = ItemFactory()
        val additional = config.properties["output"]?.map { factory.parse(it) }
        return LeatherRecipe(name, additional?.toSet() ?: setOf())
    }
}
