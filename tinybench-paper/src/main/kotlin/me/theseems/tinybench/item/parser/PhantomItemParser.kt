package me.theseems.tinybench.item.parser

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import me.theseems.tinybench.config.ItemConfig
import me.theseems.tinybench.item.Item
import me.theseems.tinybench.item.PhantomItem

class PhantomItemParser : ItemParser() {
    override fun parse(itemConfig: ItemConfig): Item {
        return PhantomItem(YAMLMapper().valueToTree(itemConfig.modifiers))
    }
}
