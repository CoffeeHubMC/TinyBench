package me.theseems.tinybench

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
        // Plugin startup logic
        TinyBenchAPI.initialize(SimpleRecipeManager())
        TinyBenchAPI.instance.recipeManager.store(ExactGridRecipeContainer())
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
