package me.theseems.tinybench.task.recipes

import me.theseems.tinybench.ExactGridRecipeContainer
import me.theseems.tinybench.TinyBenchAPI
import me.theseems.toughwiki.impl.bootstrap.BootstrapTask
import me.theseems.toughwiki.impl.bootstrap.Phase
import java.util.logging.Logger

class RecipeContainerRegisterTask : BootstrapTask("registerContainers", Phase.PRE_CONFIG) {
    override fun run(logger: Logger) {
        TinyBenchAPI.instance.recipeManager.store(ExactGridRecipeContainer())
    }
}
