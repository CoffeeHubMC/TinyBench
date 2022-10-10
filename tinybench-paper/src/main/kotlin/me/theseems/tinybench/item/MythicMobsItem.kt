package me.theseems.tinybench.item

import io.lumine.mythic.api.MythicProvider
import io.lumine.mythic.bukkit.adapters.BukkitItemStack
import org.bukkit.inventory.ItemStack

data class MythicMobsItem(private val name: String, override var amount: Int = 1) : IconItem {
    override fun clone(): Item {
        return MythicMobsItem(name, amount)
    }

    override val stack: ItemStack
        get() {
            return (
                MythicProvider.get().itemManager.getItem(name).get()
                    .generateItemStack(amount) as BukkitItemStack
                ).build()
        }

    companion object {
        fun from(itemStack: ItemStack): MythicMobsItem? {
            val type = MythicProvider.get().itemManager.getMythicTypeFromItem(itemStack) ?: return null
            return MythicMobsItem(type, itemStack.amount)
        }
    }
}
