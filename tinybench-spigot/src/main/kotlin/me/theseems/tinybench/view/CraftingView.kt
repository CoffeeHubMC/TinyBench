package me.theseems.tinybench.view

import me.theseems.tinybench.SimpleRecipeOptions.Companion.options
import me.theseems.tinybench.util.modifier
import me.theseems.tinybench.util.modifierOr
import me.theseems.tinybench.x
import me.theseems.toughwiki.ToughWiki
import me.theseems.toughwiki.api.WikiPage
import me.theseems.toughwiki.api.view.WikiPageView
import me.theseems.toughwiki.jackson.databind.ObjectMapper
import me.theseems.toughwiki.jackson.databind.node.ObjectNode
import me.theseems.toughwiki.jackson.dataformat.yaml.YAMLFactory
import me.theseems.toughwiki.utils.TextUtils
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory
import java.util.*

class CraftingView(private val page: WikiPage) : Listener, WikiPageView {
    private val playerMap: MutableMap<UUID, CraftingPlayerView> = mutableMapOf()
    private val inventoryMap: MutableMap<Inventory, CraftingPlayerView> = mutableMapOf()

    @EventHandler(priority = EventPriority.LOWEST)
    fun onClick(clickEvent: InventoryClickEvent) {
        inventoryMap[clickEvent.inventory]?.handleClick(clickEvent)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onDrag(dragEvent: InventoryDragEvent) {
        inventoryMap[dragEvent.inventory]?.handleDrag(dragEvent)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onClose(closeEvent: InventoryCloseEvent) {
        inventoryMap[closeEvent.inventory]?.handleClose(closeEvent)
    }

    override fun getPage() = page

    override fun getName(): String {
        return "craftingView"
    }

    override fun show(player: UUID) {
        if (player !in playerMap) {
            val view = CraftingPlayerView(
                player,
                page.modifierOr("recipeOptions", options(3 x 9)),
                page.modifier("recipeSlots")
                    ?: throw IllegalStateException("Recipe slots mapping is not defined"),
                page.modifier("resultSlots")
                    ?: throw IllegalStateException("Recipe's result slots mapping is not defined"),
                TextUtils.parse(page.info.title),
                page.info.size * 9
            )

            for (item in page.info.items) {
                val slot = item.modifier<Int>("slot") ?: continue
                view.inventory.setItem(slot, ToughWiki.getItemFactory().produce(Bukkit.getPlayer(player), item))
            }

            playerMap[player] = view
            inventoryMap[view.inventory] = view
        }

        playerMap[player]!!.show()
    }

    override fun dispose(player: UUID) {
        val view = playerMap[player] ?: return
        view.dispose()
        inventoryMap.remove(view.inventory)
        playerMap.remove(player)
    }

    override fun dispose() {
        inventoryMap.values.forEach { it.dispose() }
        HandlerList.unregisterAll(this)
    }

    // i reckon we don't need to add support for this, so let's make it virtually read-only
    override fun getGlobalContext() = ObjectNode(ObjectMapper(YAMLFactory()).nodeFactory)
    override fun getContext(player: UUID) = globalContext
}
