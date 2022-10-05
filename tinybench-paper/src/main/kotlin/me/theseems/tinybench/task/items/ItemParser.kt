package me.theseems.tinybench.task.items

import me.theseems.tinybench.config.ItemConfig
import me.theseems.tinybench.item.Item

abstract class ItemParser {
    abstract fun parse(itemConfig: ItemConfig): Item
}
