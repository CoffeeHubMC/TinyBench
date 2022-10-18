package me.theseems.tinybench.view.action

import me.theseems.tinybench.item.PhantomItem
import me.theseems.toughwiki.api.view.Action
import me.theseems.toughwiki.api.view.TriggerType

data class AddPhantomItemAction(val trigger: TriggerType, val phantomItem: PhantomItem, val slot: Int) : Action {
    override fun getTriggerType() = trigger
}
