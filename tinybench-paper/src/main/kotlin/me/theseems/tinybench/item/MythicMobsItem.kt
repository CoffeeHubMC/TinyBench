package me.theseems.tinybench.item

import io.lumine.mythic.api.MythicProvider
import io.lumine.mythic.bukkit.adapters.BukkitItemStack
import org.bukkit.inventory.ItemStack

data class MythicMobsItem private constructor(private val name: String, override var amount: Int = 1) : IconItem {
    private var serialized: Map<String, Any> = mapOf()

    constructor(name: String, amount: Int, serialized: Map<String, Any>) : this(name, amount) {
        this.serialized = serialized
    }

    override fun clone(): Item {
        return MythicMobsItem(name, amount, serialized)
    }

    override val stack: ItemStack
        get() = ItemStack.deserialize(serialized)

    companion object {
        fun from(itemStack: ItemStack): MythicMobsItem? {
            val type = MythicProvider.get().itemManager.getMythicTypeFromItem(itemStack) ?: return null
            return MythicMobsItem(type, itemStack.amount, itemStack.serialize())
        }

        fun from(name: String, amount: Int): MythicMobsItem {
            val stack = MythicProvider.get().itemManager.getItem(name).get().generateItemStack(amount)
            stack as BukkitItemStack
            return MythicMobsItem(name, amount, stack.build().serialize())
        }
    }
}
