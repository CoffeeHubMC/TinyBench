package me.theseems.tinybench.item.parser

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import me.theseems.tinybench.config.ItemConfig
import me.theseems.tinybench.item.Item
import me.theseems.tinybench.item.ItemStackItem
import me.theseems.toughwiki.ToughWiki
import me.theseems.toughwiki.api.WikiPageItemConfig

class BukkitItemParser : ItemParser() {
    override fun parse(itemConfig: ItemConfig): Item {
        val mapper = ObjectMapper(YAMLFactory()).registerKotlinModule()
        return ItemStackItem(
            ToughWiki.getItemFactory()
                .produce(null, mapper.readValue<WikiPageItemConfig>(mapper.writeValueAsString(itemConfig)))
        )
    }
}
