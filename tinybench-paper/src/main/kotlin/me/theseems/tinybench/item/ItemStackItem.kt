package me.theseems.tinybench.item

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

data class ItemStackItem(val itemStack: ItemStack) : IconItem {
    companion object {
        fun itemOf(itemStack: ItemStack) = ItemStackItem(itemStack)
        fun itemOf(material: Material) = ItemStackItem(ItemStack(material))
    }

    override val stack: ItemStack = itemStack

    override var amount: Int
        get() = itemStack.amount
        set(value) {
            itemStack.amount = value
        }

    override fun clone() = ItemStackItem(itemStack.clone())
}
