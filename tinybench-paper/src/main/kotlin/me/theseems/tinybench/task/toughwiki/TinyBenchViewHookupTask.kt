package me.theseems.tinybench.task.toughwiki

import me.theseems.tinybench.TinyBench
import me.theseems.tinybench.view.CraftingView
import me.theseems.tinybench.view.action.AddPhantomItemActionHandler
import me.theseems.tinybench.view.action.AddPhantomItemActionProducer
import me.theseems.toughwiki.ToughWiki
import me.theseems.toughwiki.api.ToughWikiAPI
import me.theseems.toughwiki.impl.bootstrap.BootstrapTask
import me.theseems.toughwiki.impl.bootstrap.Phase
import me.theseems.toughwiki.paper.task.PaperRegisterViewTask
import org.bukkit.Bukkit
import java.util.logging.Logger

class TinyBenchViewHookupTask : BootstrapTask("hookupCraftingView", Phase.POST_CONFIG) {
    override fun run(logger: Logger) {
        // TODO: fix toughwiki to make it so it's possible to remove producers
        ToughWiki.getActionFactory().add(AddPhantomItemActionProducer())
        ToughWikiAPI.getInstance().actionEmitter.register(AddPhantomItemActionHandler())

        for (page in ToughWikiAPI.getInstance().pageRepository.allPages) {
            if (ToughWikiAPI.getInstance().viewManager.getView(page).isPresent) {
                continue
            }
            val benchViewEnabled = PaperRegisterViewTask.getViewType(page)
                .map { value -> value.equals("craftingView") }
                .orElse(false)
            if (!benchViewEnabled) {
                continue
            }

            val view = CraftingView(page)
            Bukkit.getPluginManager().registerEvents(view, TinyBench.plugin)

            ToughWikiAPI.getInstance().viewManager.store(view)
        }
    }
}
