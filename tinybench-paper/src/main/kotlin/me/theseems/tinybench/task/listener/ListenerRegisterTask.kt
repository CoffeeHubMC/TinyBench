package me.theseems.tinybench.task.listener

import me.theseems.tinybench.TinyBench
import me.theseems.tinybench.listener.MoneyRecipeConstraintListener
import me.theseems.toughwiki.impl.bootstrap.BootstrapTask
import me.theseems.toughwiki.impl.bootstrap.Phase
import org.bukkit.Bukkit
import java.util.logging.Logger

class ListenerRegisterTask : BootstrapTask("registerListeners", Phase.POST_CONFIG) {
    companion object {
        var moneyRecipeConstraintListener: MoneyRecipeConstraintListener? = null
    }

    override fun run(logger: Logger) {
        moneyRecipeConstraintListener = MoneyRecipeConstraintListener()
        Bukkit.getServer().pluginManager.registerEvents(moneyRecipeConstraintListener!!, TinyBench.plugin)
    }
}
