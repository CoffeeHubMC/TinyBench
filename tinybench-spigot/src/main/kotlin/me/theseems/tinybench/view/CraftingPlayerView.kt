package me.theseems.tinybench.view

import me.theseems.tinybench.Slot
import me.theseems.tinybench.TinyBench
import me.theseems.tinybench.TinyBenchAPI
import me.theseems.tinybench.item.Item
import me.theseems.tinybench.item.ItemMapping
import me.theseems.tinybench.item.ItemStackItem
import me.theseems.tinybench.item.MythicMobsItem
import me.theseems.tinybench.recipe.RecipeOptions
import me.theseems.tinybench.slot
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.util.*

class CraftingPlayerView(
    private val playerUUID: UUID,
    private val options: RecipeOptions,
    private val recipeMapping: Map<Int, Int>,
    private val resultMapping: Map<Int, Int>,
    private val resultSound: Sound?,
    title: Component,
    size: Int
) {
    private var keepLeftovers: Map<Slot, Item> = mutableMapOf()
    var inventory: Inventory = Bukkit.createInventory(Bukkit.getPlayer(playerUUID), size, title)

    val reversedRecipeMapping = recipeMapping.entries.associate { (k, v) -> v to k }

    fun handleClick(event: InventoryClickEvent) {
        if (event.isCancelled) {
            return
        }

        // Nothing must happen when player clicks the empty result slot
        if (event.rawSlot < event.view.topInventory.size &&
            event.slot in resultMapping.values &&
            event.view.topInventory.getItem(event.slot) == null
        ) {
            event.isCancelled = true
            return
        }

        // Shift+Click should put selected item in a first applicable slot
        if (event.isShiftClick && event.currentItem != null && event.rawSlot >= event.view.topInventory.size) {
            event.isCancelled = true
            val item = event.currentItem!!
            for (key in recipeMapping.keys) {
                if (inventory.getItem(key) == null) {
                    event.currentItem = null
                    inventory.setItem(key, item)
                    return
                } else if (inventory.getItem(key)!!
                    .isSimilar(item) && inventory.getItem(key)!!.amount + item.amount <= item.maxStackSize
                ) {
                    val located = inventory.getItem(key)
                    located!!.amount += item.amount
                    event.currentItem = null
                    return
                }
            }
            return
        }

        // Schedule recipe processing logic to the next tick
        tryCraft(event)

        // We don't care if player clicks and item inside their inventory
        if (event.rawSlot >= event.view.topInventory.size) {
            return
        }
        // Player must not click anything in our GUI besides slots for recipe and for results
        if (event.slot !in recipeMapping && event.slot !in resultMapping.values) {
            event.isCancelled = true
            return
        }
        // We now don't care if player clicks a slot for recipe
        if (event.slot in recipeMapping) {
            return
        }

        // When player clicks a slot which stands for result, and it's not empty
        // then we immediately clear recipe slots and transfer all the results
        // to their inventory
        if (event.slot in resultMapping.values && event.view.topInventory.getItem(event.slot) != null) {
            if (event.cursor?.type != Material.AIR) {
                event.isCancelled = true
                return
            }
            recipeMapping.keys.forEach { inventory.setItem(it, null) }
            pickItems(resultMapping.values)
            keepLeftovers.forEach { (slot, item) ->
                reversedRecipeMapping[options.size.slot(slot)]?.let {
                    inventory.setItem(
                        it,
                        makeItemStackOutOfItem(item)
                    )
                }
            }

            resultSound?.let { event.whoClicked.playSound(it) }
            return
        }
    }

    fun handleDrag(event: InventoryDragEvent) {
        event.newItems.forEach {
            if (it.key < event.view.topInventory.size && it.key !in recipeMapping) {
                event.isCancelled = true
                return@forEach
            }
        }

        tryCraft(event)
    }

    private fun tryCraft(event: Cancellable) {
        Bukkit.getScheduler().runTask(
            TinyBench.plugin,
            Runnable {
                if (event.isCancelled) {
                    return@Runnable
                }

                val (produced, leftovers) = TinyBenchAPI.instance.recipeManager.produce(makeMapping(), options)
                if (produced.isNotEmpty()) {
                    produced.forEach {
                        inventory.setItem(
                            resultMapping[options.size.slot(it.key)]
                                ?: throw IllegalStateException("No slot for reward"),
                            makeItemStackOutOfItem(it.value)
                        )
                    }
                    keepLeftovers = leftovers
                } else {
                    keepLeftovers = mutableMapOf()
                    resultMapping.forEach { inventory.setItem(it.value, null) }
                }
            }
        )
    }

    fun handleClose(event: InventoryCloseEvent) {
        dispose(closed = true)
    }

    private fun pickItems(items: Collection<Int>) {
        val player = Bukkit.getPlayer(playerUUID)
            ?: throw IllegalStateException(
                "There's no player to give items ($items) to. Expected $playerUUID (${
                Bukkit.getOfflinePlayer(
                    playerUUID
                ).name
                })"
            )

        items.forEach {
            val item = inventory.getItem(it) ?: return@forEach
            inventory.setItem(it, null)
            player.inventory.addItem(item)
        }
    }

    fun show() {
        Bukkit.getPlayer(playerUUID)?.openInventory(inventory)
    }

    fun dispose(closePlayer: Player? = null, closed: Boolean = false) {
        val player = closePlayer ?: Bukkit.getPlayer(playerUUID)

        for (entry in resultMapping) {
            inventory.setItem(entry.value, null)
        }

        for (entry in recipeMapping) {
            val item = inventory.getItem(entry.key)
            if (item != null) {
                inventory.setItem(entry.key, null)
                player?.inventory?.addItem(item)
            }
        }

        if (!closed) {
            ArrayList(inventory.viewers)
                .forEach { it.closeInventory(InventoryCloseEvent.Reason.PLUGIN) }
        }
    }

    private fun makeMapping(additional: Map<Int, ItemStack>? = null): ItemMapping {
        val result: MutableMap<Slot, Item> = mutableMapOf()
        recipeMapping.forEach {
            val itemStack = inventory.getItem(it.key) ?: additional?.get(it.key) ?: return@forEach
            val item = makeItemOutOfItemStack(itemStack)
            result[options.size.slot(it.value)] = item
        }
        return result
    }

    private fun makeItemOutOfItemStack(itemStack: ItemStack): Item {
        // TODO: extend
        return MythicMobsItem.from(itemStack) ?: ItemStackItem(itemStack)
    }

    private fun makeItemStackOutOfItem(item: Item): ItemStack? {
        // TODO: extend
        if (item is ItemStackItem) {
            return item.itemStack
        }
        if (item is MythicMobsItem) {
            return item.stack
        }
        return null
    }
}
