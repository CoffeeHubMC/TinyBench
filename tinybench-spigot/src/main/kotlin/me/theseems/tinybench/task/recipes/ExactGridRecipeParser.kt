package me.theseems.tinybench.task.recipes

import com.fasterxml.jackson.databind.JsonNode
import me.theseems.tinybench.ExactGridRecipe
import me.theseems.tinybench.GridContainer
import me.theseems.tinybench.config.RecipeConfig
import me.theseems.tinybench.slot
import me.theseems.tinybench.task.items.ItemFactory
import me.theseems.tinybench.toGridContainer
import me.theseems.tinybench.util.modifier
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

        return ExactGridRecipe(name, getGridContainer("input"), getGridContainer("output"))
    }
}
