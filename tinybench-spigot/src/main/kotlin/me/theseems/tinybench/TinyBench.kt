package me.theseems.tinybench

import me.theseems.tinybench.config.VanillaRecipeConfig
import me.theseems.tinybench.task.APIInitTask
import me.theseems.tinybench.task.CommandRegisterTask
import me.theseems.tinybench.task.RecipeContainerRegisterTask
import me.theseems.tinybench.task.RecipeParseTask
import me.theseems.tinybench.task.RemoveDisabledRecipesTask
import me.theseems.tinybench.task.TinyBenchViewHookupTask
import me.theseems.tinybench.task.UnregisterRecipesTask
import me.theseems.tinybench.task.UnregisterViewTask
import me.theseems.tinybench.task.VanillaRecipeConfigParseTask
import me.theseems.toughwiki.ToughWiki
import me.theseems.toughwiki.impl.bootstrap.Phase
import me.theseems.toughwiki.impl.bootstrap.ToughWikiBootstrap
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.JavaPluginLoader
import java.io.File

open class TinyBench : JavaPlugin {
    protected constructor(
        loader: JavaPluginLoader?,
        description: PluginDescriptionFile?,
        dataFolder: File?,
        file: File?
    ) : super(loader!!, description!!, dataFolder!!, file!!)

    public constructor() : super()

    override fun onEnable() {
        plugin = this

        bootstrap = ToughWikiBootstrap(plugin.logger)
        bootstrap.add(APIInitTask())
        bootstrap.add(RecipeContainerRegisterTask())
        bootstrap.add(RecipeParseTask(File(dataFolder, "recipes")))
        bootstrap.add(VanillaRecipeConfigParseTask(File(dataFolder, "vanilla.yml")))
        bootstrap.add(RemoveDisabledRecipesTask())
        bootstrap.add(CommandRegisterTask())
        bootstrap.add(UnregisterRecipesTask())
        bootstrap.add(UnregisterViewTask())

        bootstrap.execute(Phase.PRE_CONFIG, Phase.CONFIG, Phase.POST_CONFIG)
        if (ToughWiki.getBootstrap() != null) {
            val task = TinyBenchViewHookupTask()
            ToughWiki.getBootstrap().add(task)
            task.run(plugin.logger)
        }
    }

    companion object {
        lateinit var plugin: TinyBench
        lateinit var bootstrap: ToughWikiBootstrap
        lateinit var vanillaRecipeConfig: VanillaRecipeConfig
    }
}
