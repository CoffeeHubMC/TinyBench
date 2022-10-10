package me.theseems.tinybench.view.preview

import me.theseems.tinybench.TinyBench
import me.theseems.tinybench.TinyBenchAPI
import me.theseems.tinybench.recipeManager
import me.theseems.toughwiki.api.WikiPage
import me.theseems.toughwiki.api.view.WikiPageView
import me.theseems.toughwiki.api.view.WikiPageViewFactory
import java.util.*

class IFPreviewViewFactory : WikiPageViewFactory {
    override fun getType() = typeName

    override fun produce(page: WikiPage, name: String): Optional<WikiPageView> {
        val previewConfig = TinyBench.previewConfig.entries[name] ?: return Optional.empty()
        val recipe = TinyBenchAPI.recipeManager.getRecipe(previewConfig.recipe)
        if (recipe == null) {
            TinyBench.plugin.logger.warning("Recipe ${previewConfig.recipe} for the preview $name does not exist")
            return Optional.empty()
        }

        return Optional.of(CraftingPreviewView(page, recipe, previewConfig.items ?: listOf()))
    }

    companion object {
        const val typeName = "tinybenchPreview"
    }
}
