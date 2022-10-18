package me.theseems.tinybench.task.listener

import me.theseems.toughwiki.impl.bootstrap.BootstrapTask
import me.theseems.toughwiki.impl.bootstrap.Phase
import org.bukkit.event.HandlerList
import java.util.logging.Logger

class ListenerUnregisterTask : BootstrapTask("unregisterListeners", Phase.SHUTDOWN) {
    override fun run(logger: Logger) {
        ListenerRegisterTask.moneyRecipeConstraintListener?.let { HandlerList.unregisterAll(it) }
    }
}
