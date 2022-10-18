package me.theseems.tinybench.recipes

import com.fasterxml.jackson.databind.JsonNode
import me.theseems.tinybench.TinyBench
import me.theseems.tinybench.config.RecipeConfig
import me.theseems.tinybench.item.Item
import me.theseems.tinybench.item.parser.ItemFactory
import me.theseems.tinybench.shapeless.ShapelessRecipe
import me.theseems.tinybench.util.modifier
import java.util.logging.Logger

class ShapelessRecipeParser : RecipeParser<ShapelessRecipe>() {
    override fun makeRecipe(name: String, config: RecipeConfig, logger: Logger): ShapelessRecipe {
        val factory = ItemFactory()
        fun getItemSet(key: String): HashSet<Item> {
            val result = config.modifier<List<JsonNode>>(key)
                ?.map { factory.parse(it) }
                ?.toHashSet()
            if (result == null) {
                TinyBench.plugin.logger.warning("Empty set got @ $key for a shapeless recipe $name")
                return hashSetOf()
            }
            return result
        }

        return ShapelessRecipe(
            name,
            getItemSet("input"),
            getItemSet("output")
        )
    }
}
