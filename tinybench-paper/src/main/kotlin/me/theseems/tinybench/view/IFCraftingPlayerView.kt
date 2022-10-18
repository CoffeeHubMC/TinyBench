package me.theseems.tinybench.view

import me.theseems.tinybench.Slot
import me.theseems.tinybench.TinyBench
import me.theseems.tinybench.TinyBenchAPI
import me.theseems.tinybench.event.RecipeCraftEvent
import me.theseems.tinybench.item.Item
import me.theseems.tinybench.item.ItemMapping
import me.theseems.tinybench.item.ItemStackItem
import me.theseems.tinybench.item.MythicMobsItem
import me.theseems.tinybench.recipe.Recipe
import me.theseems.tinybench.recipe.RecipeOptions
import me.theseems.tinybench.slot
import me.theseems.toughwiki.inventoryframework.adventuresupport.ComponentHolder
import me.theseems.toughwiki.inventoryframework.gui.type.ChestGui
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.ItemStack
import java.util.*

open class IFCraftingPlayerView(
    private val playerUUID: UUID,
    private val options: RecipeOptions,
    private val recipeMapping: Map<Int, Int>,
    private val resultMapping: Map<Int, Int>,
    private val resultSound: Sound?,
    private val blockedSlots: Set<Int>,
    title: Component,
    size: Int
) {
    private val additionalItems: MutableMap<Slot, Item> = mutableMapOf()
    private var keepResults: Map<Slot, Item> = mutableMapOf()
    private var keepLeftovers: Map<Slot, Item> = mutableMapOf()
    private var keepRecipe: Recipe? = null
    var chestGui: ChestGui = ChestGui(size, ComponentHolder.of(title))

    private val reversedRecipeMapping = recipeMapping.entries.associate { (k, v) -> v to k }

    init {
        chestGui.setOnClose { dispose(closePlayer = it.player as? Player) }
        chestGui.setOnGlobalClick { handleClick(it) }
        chestGui.setOnGlobalDrag { handleDrag(it) }
    }

    fun fitAdditional(item: Item): Slot? {
        for (i in 0 until options.size.height) {
            for (j in 0 until options.size.width) {
                val slot = Slot(i, j)
                if (chestGui.inventory.getItem(options.size.slot(slot)) == null && slot !in additionalItems) {
                    additionalItems[slot] = item
                    return slot
                }
            }
        }
        return null
    }

    fun clearAdditional(predicate: (Item) -> Boolean) {
        val slotList = mutableListOf<Slot>()
        additionalItems.forEach { (slot, item) ->
            if (predicate(item)) {
                slotList += slot
            }
        }
        slotList.forEach { additionalItems.remove(it) }
    }

    protected open fun handleClick(event: InventoryClickEvent) {
        if (event.isCancelled) {
            return
        }

        // Schedule recipe processing logic to the next tick
        tryCraft()

        // Nothing must happen when player clicks the empty result slot
        if (event.rawSlot < event.view.topInventory.size &&
            event.slot in resultMapping.values &&
            event.view.topInventory.getItem(event.slot) == null
        ) {
            event.isCancelled = true
            return
        }
        // Shift+Click should put selected item in a first applicable slot
        // fucking spaghetti
        if (event.isShiftClick && event.currentItem != null && event.rawSlot >= event.view.topInventory.size) {
            event.isCancelled = true
            val item = event.currentItem!!
            for (key in recipeMapping.keys) {
                if (chestGui.inventory.getItem(key) == null) {
                    event.currentItem = null
                    chestGui.inventory.setItem(key, item)
                    return
                } else if (chestGui.inventory.getItem(key)!!
                    .isSimilar(item) && chestGui.inventory.getItem(key)!!.amount + item.amount <= item.maxStackSize
                ) {
                    val located = chestGui.inventory.getItem(key)
                    located!!.amount += item.amount
                    event.currentItem = null
                    return
                }
            }
            return
        }

        // We don't care if player clicks and item inside their inventory
        if (event.rawSlot >= event.view.topInventory.size) {
            return
        }
        if (event.slot in blockedSlots) {
            event.isCancelled = true
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

            // Fire and check an event
            val craftEvent = RecipeCraftEvent(
                keepRecipe,
                event.whoClicked.uniqueId,
                makeMapping(),
                keepResults,
                keepLeftovers
            )
            Bukkit.getPluginManager().callEvent(craftEvent)
            if (craftEvent.isCancelled) {
                event.isCancelled = true
                return
            }

            additionalItems.clear()
            recipeMapping.keys.forEach { chestGui.inventory.setItem(it, null) }
            pickItems(resultMapping.values)
            keepLeftovers.forEach { (slot, item) ->
                reversedRecipeMapping[options.size.slot(slot)]?.let {
                    chestGui.inventory.setItem(
                        it,
                        makeItemStackOutOfItem(item)
                    )
                }
            }

            resultSound?.let { event.whoClicked.playSound(it) }
            return
        }
    }

    protected open fun handleDrag(event: InventoryDragEvent) {
        // Schedule recipe processing logic to the next tick
        tryCraft()

        event.newItems.forEach {
            if (it.key < event.view.topInventory.size && (it.key !in recipeMapping || it.key in blockedSlots)) {
                event.isCancelled = true
                return@forEach
            }
        }
    }

    protected open fun tryCraft() {
        Bukkit.getScheduler().runTask(
            TinyBench.plugin,
            Runnable {
                val (produced, leftovers, recipe) = TinyBenchAPI.instance.recipeManager.produce(makeMapping(), options)
                if (produced.isNotEmpty()) {
                    produced.forEach {
                        chestGui.inventory.setItem(
                            resultMapping[options.size.slot(it.key)]
                                ?: throw IllegalStateException("No slot for reward"),
                            makeItemStackOutOfItem(it.value)
                        )
                    }
                    keepResults = produced
                    keepLeftovers = leftovers
                    keepRecipe = recipe
                } else {
                    resultMapping.forEach { chestGui.inventory.setItem(it.value, null) }
                    keepLeftovers = mutableMapOf()
                    keepResults = mutableMapOf()
                    keepRecipe = null
                }
            }
        )
    }

    protected open fun pickItems(items: Collection<Int>) {
        val player = Bukkit.getPlayer(playerUUID)
            ?: throw IllegalStateException(
                "There's no player to give items ($items) to. Expected $playerUUID (${
                Bukkit.getOfflinePlayer(
                    playerUUID
                ).name
                })"
            )

        items.forEach {
            val item = chestGui.inventory.getItem(it) ?: return@forEach
            chestGui.inventory.setItem(it, null)
            player.inventory.addItem(item)
        }
    }

    fun show() {
        Bukkit.getPlayer(playerUUID)?.let { chestGui.show(it) }
    }

    fun dispose(closePlayer: Player? = null, closed: Boolean = false) {
        val player = closePlayer ?: Bukkit.getPlayer(playerUUID)

        keepRecipe = null
        keepResults = mutableMapOf()
        keepLeftovers = mutableMapOf()
        additionalItems.clear()

        for (entry in resultMapping) {
            chestGui.inventory.setItem(entry.value, null)
        }

        for (entry in recipeMapping) {
            val item = chestGui.inventory.getItem(entry.key)
            if (item != null) {
                chestGui.inventory.setItem(entry.key, null)
                player?.inventory?.addItem(item)
            }
        }

        if (!closed) {
            ArrayList(chestGui.inventory.viewers)
                .forEach { it.closeInventory(InventoryCloseEvent.Reason.PLUGIN) }
        }
    }

    private fun makeMapping(additional: Map<Int, ItemStack>? = null): ItemMapping {
        val result: MutableMap<Slot, Item> = mutableMapOf()
        recipeMapping.forEach {
            if (it.value in blockedSlots) {
                return@forEach
            }

            val itemStack = chestGui.inventory.getItem(it.key) ?: additional?.get(it.key) ?: return@forEach
            val item = makeItemOutOfItemStack(itemStack)
            val slot = options.size.slot(it.value)
            additionalItems.remove(slot)

            result[slot] = item
        }
        additionalItems.forEach { (slot, item) -> result[slot] = item }
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
