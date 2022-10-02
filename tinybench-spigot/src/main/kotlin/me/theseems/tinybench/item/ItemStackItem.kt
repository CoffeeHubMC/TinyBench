package me.theseems.tinybench.item

import me.theseems.tinybench.Item
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

data class ItemStackItem(val itemStack: ItemStack) : Item {
    companion object {
        fun itemOf(itemStack: ItemStack) = ItemStackItem(itemStack)
        fun itemOf(material: Material) = ItemStackItem(ItemStack(material))
    }
}
