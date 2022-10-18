package me.theseems.tinybench.recipes

import com.fasterxml.jackson.databind.JsonNode
import me.theseems.tinybench.ExactGridRecipe
import me.theseems.tinybench.GridContainer
import me.theseems.tinybench.config.RecipeConfig
import me.theseems.tinybench.item.parser.ItemFactory
import me.theseems.tinybench.slot
import me.theseems.tinybench.toGridContainer
import me.theseems.tinybench.util.modifier
import me.theseems.tinybench.util.modifierOr
import java.util.logging.Logger

class ExactGridRecipeParser : RecipeParser<ExactGridRecipe>() {
    override fun makeRecipe(name: String, config: RecipeConfig, logger: Logger): ExactGridRecipe {
        val factory = ItemFactory()

        fun getGridContainer(key: String): GridContainer {
            val mapping = config.modifier<Map<Int, JsonNode>>(key)
                ?.mapKeys { config.size.slot(it.key) }
                ?.mapValues { factory.parse(it.value) }
            return mapping?.toGridContainer(config.size)
                ?: throw IllegalStateException("No grid container was found. Check 'input' property of the recipe config ($name')")
        }

        return ExactGridRecipe(
            name,
            config.modifierOr("stackable", true),
            getGridContainer("input"),
            getGridContainer("output")
        )
    }
}
