package me.theseems.tinybench.task

import me.theseems.tinybench.TinyBench
import me.theseems.toughwiki.impl.bootstrap.BootstrapTask
import me.theseems.toughwiki.impl.bootstrap.Phase
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import java.util.logging.Logger

class RemoveDisabledRecipesTask : BootstrapTask("removeRecipes", Phase.POST_CONFIG) {
    override fun run(logger: Logger) {
        TinyBench.vanillaRecipeConfig.blockedRecipes?.forEach {
            NamespacedKey.fromString(it)
                ?.let { recipeKey -> Bukkit.getServer().removeRecipe(recipeKey) }
                ?: logger.warning("Invalid recipe specified: $it")
        }
    }
}
