package me.theseems.tinybench.view.action

import me.theseems.tinybench.item.PhantomItem
import me.theseems.tinybench.view.IFCraftingPlayerView
import me.theseems.toughwiki.api.view.ActionSender
import me.theseems.toughwiki.paper.view.action.BaseWikiPageActionHandler

class AddPhantomItemActionHandler : BaseWikiPageActionHandler<AddPhantomItemAction>(AddPhantomItemAction::class.java) {
    override fun handle(action: AddPhantomItemAction, sender: ActionSender) {
        val view = sender.container.getValue<IFCraftingPlayerView>("playerView") ?: return
        val phantom = action.phantomItem
        view.clearAdditional {
            if (it !is PhantomItem) {
                return@clearAdditional false
            }

            val firstIter = it.content.iterator()
            val secondIter = phantom.content.iterator()

            while (firstIter.hasNext() && secondIter.hasNext()) {
                if (firstIter.next() != secondIter.next()) {
                    return@clearAdditional false
                }
            }

            return@clearAdditional !firstIter.hasNext() && !secondIter.hasNext()
        }
        view.fitAdditional(phantom)
    }
}
