package me.theseems.tinybench.item.parser

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import me.theseems.tinybench.TinyBench
import me.theseems.tinybench.config.ItemConfig
import me.theseems.tinybench.item.Item
import org.bukkit.Material

class ItemFactory {
    private val map = mapOf(
        "bukkit" to BukkitItemParser(),
        "mythic" to MythicItemParser(),
        "phantom" to PhantomItemParser()
    )

    fun parse(jsonNode: JsonNode): Item {
        if (jsonNode.isTextual) {
            val split = jsonNode.textValue().split(":")
            if (split.size == 1) {
                return map["bukkit"]!!.parse(
                    ItemConfig(
                        Material.valueOf(split[0]).name,
                        mutableMapOf()
                    )
                )
            }

            val mapper = ObjectMapper()
            val inlineMap = mutableMapOf<String, JsonNode>()
            split[1].split(",").forEach {
                val segment = it.split("=")
                if (segment.size != 2) {
                    TinyBench.plugin.logger.warning("Unknown inline definition: $segment")
                    return@forEach
                }

                val (name, value) = segment
                inlineMap[name] = mapper.readTree(value.replace("\'", "\""))
            }

            return map[split[0]]?.parse(ItemConfig(jsonNode.textValue(), inlineMap))
                ?: throw IllegalStateException("No such type '" + jsonNode.textValue())
        } else {
            val itemConfig =
                ObjectMapper(YAMLFactory()).registerKotlinModule().readValue<ItemConfig>(jsonNode.toString())
            return map[itemConfig.type]?.parse(itemConfig)
                ?: throw IllegalStateException("No such type '" + itemConfig.type + "'")
        }
    }
}
