package me.theseems.tinybench.task.preview

import me.theseems.tinybench.ExactGridRecipe
import me.theseems.tinybench.TinyBench
import me.theseems.tinybench.TinyBenchAPI
import me.theseems.tinybench.recipeManager
import me.theseems.tinybench.view.preview.IFPreviewViewFactory
import me.theseems.toughwiki.api.ToughWikiAPI
import me.theseems.toughwiki.impl.SimpleWikiPage
import me.theseems.toughwiki.impl.bootstrap.BootstrapTask
import me.theseems.toughwiki.impl.bootstrap.Phase
import me.theseems.toughwiki.impl.view.SimpleWikiPageViewManager
import java.util.logging.Logger

class RecipePageRegisterTask : BootstrapTask("registerRecipePages", Phase.POST_CONFIG) {
    override fun run(logger: Logger) {
        TinyBench.previewConfig.entries.forEach { (name, config) ->
            val recipe = TinyBenchAPI.recipeManager.getRecipe(config.recipe)
            if (recipe == null) {
                logger.warning("Recipe '${config.recipe}' specified in the preview config does not exist")
                return@forEach
            }
            if (recipe !is ExactGridRecipe) {
                logger.warning("Preview for recipe '${config.recipe}' (${recipe.javaClass.name}) is not yet supported")
                return@forEach
            }

            val page = ToughWikiAPI.getInstance().pageRepository.getPage(config.page).orElse(null)
            if (page == null) {
                logger.warning("Page '${config.page}' specified for recipe '${config.recipe}' preview does not exist")
                return@forEach
            }

            ToughWikiAPI.getInstance().pageRepository.store(
                SimpleWikiPage(
                    IFPreviewViewFactory.typeName + SimpleWikiPageViewManager.VIEW_FACTORY_SEPARATOR + name,
                    null,
                    linkedSetOf(),
                    page.info
                )
            )
        }
    }
}
