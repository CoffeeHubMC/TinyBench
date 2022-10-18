package me.theseems.tinybench.item.parser

import me.theseems.tinybench.config.ItemConfig
import me.theseems.tinybench.item.Item
import me.theseems.tinybench.item.MythicMobsItem

class MythicItemParser : ItemParser() {
    override fun parse(itemConfig: ItemConfig): Item {
        return MythicMobsItem(
            itemConfig.type.split(":")[1],
            itemConfig.modifiers["amound"]?.asInt(1) ?: 1
        )
    }
}
