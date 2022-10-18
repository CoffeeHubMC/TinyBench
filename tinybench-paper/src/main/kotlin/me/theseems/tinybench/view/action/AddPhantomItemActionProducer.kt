package me.theseems.tinybench.view.action

import com.fasterxml.jackson.databind.ObjectMapper
import me.theseems.tinybench.TinyBench
import me.theseems.tinybench.item.PhantomItem
import me.theseems.tinybench.item.parser.ItemFactory
import me.theseems.toughwiki.api.view.Action
import me.theseems.toughwiki.api.view.TriggerType
import me.theseems.toughwiki.jackson.databind.node.ObjectNode
import me.theseems.toughwiki.paper.view.action.factory.ActionProducer
import java.util.*

class AddPhantomItemActionProducer : ActionProducer {
    override fun produce(triggerType: TriggerType, node: ObjectNode): Optional<Action> {
        if (!node.has("phantomItem")) {
            return Optional.empty()
        }
        val item = ItemFactory().parse(ObjectMapper().readTree(node["phantomItem"].toString()))
        if (item !is PhantomItem) {
            TinyBench.plugin.logger.warning("Produced $item instead of phantom item")
            return Optional.empty()
        }
        val slot = node["phantomItemSlot"]?.asInt()
        if (slot == null) {
            TinyBench.plugin.logger.warning("No slot was found for a phantom item")
            return Optional.empty()
        }
        return Optional.of(AddPhantomItemAction(triggerType, item, slot))
    }
}
