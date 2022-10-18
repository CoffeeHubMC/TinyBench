package me.theseems.tinybench.task.toughwiki

import me.theseems.tinybench.view.CraftingView
import me.theseems.toughwiki.api.ToughWikiAPI
import me.theseems.toughwiki.impl.bootstrap.BootstrapTask
import me.theseems.toughwiki.impl.bootstrap.Phase
import java.util.logging.Logger

class UnregisterViewTask : BootstrapTask("unregisterCraftViews", Phase.SHUTDOWN) {
    override fun run(logger: Logger) {
        val viewManager = ToughWikiAPI.getInstance().viewManager
        for (view in viewManager.allViews) {
            if (view is CraftingView) {
                viewManager.dispose(view.page.name)
            }
        }
    }
}
