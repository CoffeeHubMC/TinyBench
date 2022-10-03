package me.theseems.tinybench.task.items

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import me.theseems.tinybench.config.ItemConfig
import me.theseems.tinybench.item.Item
import org.bukkit.Material

class ItemFactory {
    private val map = mapOf<String, ItemParser>(
        "bukkit" to BukkitItemParser()
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

            if (split[0] == "mythic") {
                // TODO:
            }

            throw IllegalStateException("No such type '" + jsonNode.textValue())
        } else {
            println("HAHA LOL $jsonNode")
            val itemConfig =
                ObjectMapper(YAMLFactory()).registerKotlinModule().readValue<ItemConfig>(jsonNode.toString())
            return map[itemConfig.type]?.parse(itemConfig)
                ?: throw IllegalStateException("No such type '" + itemConfig.type + "'")
        }
    }
}
