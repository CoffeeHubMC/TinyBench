package me.theseems.tinybench

import me.theseems.tinybench.config.PreviewConfig
import me.theseems.tinybench.config.VanillaRecipeConfig
import me.theseems.tinybench.task.APIInitTask
import me.theseems.tinybench.task.CommandRegisterTask
import me.theseems.tinybench.task.RecipeContainerRegisterTask
import me.theseems.tinybench.task.RecipeParseTask
import me.theseems.tinybench.task.RemoveDisabledVanillaRecipesTask
import me.theseems.tinybench.task.TinyBenchViewHookupTask
import me.theseems.tinybench.task.UnregisterRecipesTask
import me.theseems.tinybench.task.UnregisterViewTask
import me.theseems.tinybench.task.VanillaRecipeConfigParseTask
import me.theseems.tinybench.task.preview.PreviewConfigParseTask
import me.theseems.tinybench.task.preview.PreviewFactoryRegisterTask
import me.theseems.tinybench.task.preview.PreviewFactoryUnregisterTask
import me.theseems.tinybench.task.preview.RecipePageRegisterTask
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
        bootstrap.add(PreviewConfigParseTask(File(dataFolder, "preview.yml")))
        bootstrap.add(RemoveDisabledVanillaRecipesTask())
        bootstrap.add(CommandRegisterTask())

        bootstrap.add(UnregisterRecipesTask())
        bootstrap.add(UnregisterViewTask())

        val hookupTask = TinyBenchViewHookupTask()
        bootstrap.add(hookupTask)

        val previewFactoryRegisterTask = PreviewFactoryRegisterTask()
        bootstrap.add(previewFactoryRegisterTask)
        bootstrap.add(PreviewFactoryUnregisterTask())

        val previewPageRegisterTask = RecipePageRegisterTask()
        bootstrap.add(previewPageRegisterTask)

        bootstrap.execute(Phase.PRE_CONFIG, Phase.CONFIG, Phase.POST_CONFIG)
        if (ToughWiki.getBootstrap() != null) {
            ToughWiki.getBootstrap().add(hookupTask)
            ToughWiki.getBootstrap().add(previewPageRegisterTask)
            ToughWiki.getBootstrap().add(previewFactoryRegisterTask)
        }
    }

    companion object {
        lateinit var plugin: TinyBench
        lateinit var bootstrap: ToughWikiBootstrap
        lateinit var vanillaRecipeConfig: VanillaRecipeConfig
        lateinit var previewConfig: PreviewConfig
    }
}
