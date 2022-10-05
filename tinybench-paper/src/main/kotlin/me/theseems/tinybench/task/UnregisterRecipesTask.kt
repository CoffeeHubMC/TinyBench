package me.theseems.tinybench.task

import me.theseems.tinybench.TinyBenchAPI
import me.theseems.tinybench.recipeManager
import me.theseems.toughwiki.impl.bootstrap.BootstrapTask
import me.theseems.toughwiki.impl.bootstrap.Phase
import java.util.logging.Logger

class UnregisterRecipesTask : BootstrapTask("unregisterRecipes", Phase.SHUTDOWN) {
    override fun run(logger: Logger) {
        for (recipeContainer in TinyBenchAPI.recipeManager.getAll()) {
            TinyBenchAPI.recipeManager.dispose(recipeContainer.name)
        }
    }
}
