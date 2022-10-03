package me.theseems.tinybench

import me.theseems.tinybench.task.APIInitTask
import me.theseems.tinybench.task.RecipeContainerRegisterTask
import me.theseems.tinybench.task.RecipeParseTask
import me.theseems.tinybench.task.TinyBenchViewHookupTask
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

        val bootstrap = ToughWikiBootstrap(plugin.logger)
        bootstrap.add(APIInitTask())
        bootstrap.add(RecipeContainerRegisterTask())
        bootstrap.add(RecipeParseTask(File(dataFolder, "recipes")))

        val task = TinyBenchViewHookupTask()
        ToughWiki.getBootstrap().add(task)

        bootstrap.execute(Phase.PRE_CONFIG, Phase.CONFIG, Phase.POST_CONFIG)
        task.run(plugin.logger)
    }

    companion object {
        lateinit var plugin: TinyBench
    }
}
