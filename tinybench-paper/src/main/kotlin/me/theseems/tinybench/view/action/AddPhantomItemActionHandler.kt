package me.theseems.tinybench.view.action

import me.theseems.tinybench.view.IFCraftingPlayerView
import me.theseems.toughwiki.api.view.ActionSender
import me.theseems.toughwiki.paper.view.action.BaseWikiPageActionHandler

class AddPhantomItemActionHandler : BaseWikiPageActionHandler<AddPhantomItemAction>(AddPhantomItemAction::class.java) {
    override fun handle(action: AddPhantomItemAction, sender: ActionSender) {
        val view = sender.container.getValue<IFCraftingPlayerView>("playerView") ?: return
        val phantom = action.phantomItem
        view.clearAdditional { it == phantom }
        view.fitAdditional(phantom)
    }
}
