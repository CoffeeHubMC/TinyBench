package me.theseems.tinybench.task.recipes

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import me.theseems.tinybench.TinyBenchAPI
import me.theseems.tinybench.config.RecipeConfig
import me.theseems.tinybench.config.RecipeSectionConfig
import me.theseems.tinybench.recipeManager
import me.theseems.tinybench.recipes.ExactGridRecipeParser
import me.theseems.tinybench.recipes.LeatherRecipeParser
import me.theseems.tinybench.recipes.RecipeParser
import me.theseems.tinybench.recipes.ShapelessRecipeParser
import me.theseems.tinybench.register
import me.theseems.toughwiki.impl.bootstrap.BootstrapTask
import me.theseems.toughwiki.impl.bootstrap.Phase
import java.io.File
import java.nio.file.Files
import java.util.logging.Logger
import kotlin.streams.asSequence

class RecipeParseTask(private val folder: File) : BootstrapTask("registerRecipes", Phase.CONFIG) {
    val recipeParsers = mutableMapOf<String, RecipeParser<*>>(
        "exact" to ExactGridRecipeParser(),
        "shapeless" to ShapelessRecipeParser(),
        "leather" to LeatherRecipeParser()
    )

    override fun run(logger: Logger) {
        if (!folder.exists()) {
            folder.mkdirs()
        }

        val mapper = ObjectMapper(YAMLFactory()).registerKotlinModule()
        var totalForeignRecipesCount = 0

        Files.walk(folder.toPath())
            .asSequence()
            .filter(Files::isRegularFile)
            .filter { it.toString().endsWith(".yml") }
            .map { mapper.readValue<RecipeSectionConfig>(it.toFile()) }
            .map { it.recipes }
            .fold(mutableMapOf<String, RecipeConfig>()) { map, map2 ->
                val intersection = map.keys.intersect(map2.keys)
                if (intersection.isNotEmpty()) {
                    logger.warning("There are intersections between names of the recipes: $intersection")
                    intersection.forEach {
                        map.remove(it)
                        map2.remove(it)
                    }
                }
                map.putAll(map2)
                return@fold map
            }
            .forEach { (name, config) ->
                if (config.type !in recipeParsers) {
                    logger.warning("There is no such recipe type as \"${config.type}\"")
                    return@forEach
                }

                val recipe = recipeParsers[config.type]!!.makeRecipe(name, config, logger)
                TinyBenchAPI.recipeManager.register(config.type, recipe)

                totalForeignRecipesCount++
            }

        logger.info("Total amount of recipes loaded: $totalForeignRecipesCount")
    }
}
