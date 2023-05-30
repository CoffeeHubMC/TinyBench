package me.theseems.tinybench.view

import java.util.EnumMap
import java.util.UUID
import me.theseems.tinybench.SimpleRecipeOptions.Companion.options
import me.theseems.tinybench.slot
import me.theseems.tinybench.util.modifier
import me.theseems.tinybench.util.modifierOr
import me.theseems.tinybench.x
import me.theseems.toughwiki.ToughWiki
import me.theseems.toughwiki.api.ToughWikiAPI
import me.theseems.toughwiki.api.WikiPage
import me.theseems.toughwiki.api.view.Action
import me.theseems.toughwiki.api.view.ActionSender
import me.theseems.toughwiki.api.view.TriggerType
import me.theseems.toughwiki.api.view.WikiPageView
import me.theseems.toughwiki.impl.component.SimpleComponentContainer
import me.theseems.toughwiki.inventoryframework.gui.GuiItem
import me.theseems.toughwiki.inventoryframework.pane.StaticPane
import me.theseems.toughwiki.jackson.databind.ObjectMapper
import me.theseems.toughwiki.jackson.databind.node.ObjectNode
import me.theseems.toughwiki.jackson.dataformat.yaml.YAMLFactory
import me.theseems.toughwiki.utils.TextUtils
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.player.PlayerQuitEvent

class CraftingView(private val page: WikiPage) : Listener, WikiPageView {
    private val playerMap: MutableMap<UUID, IFCraftingPlayerView> = mutableMapOf()

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onLeave(playerQuitEvent: PlayerQuitEvent) {
        playerMap[playerQuitEvent.player.uniqueId]?.dispose(closePlayer = playerQuitEvent.player)
    }

    override fun getPage() = page

    override fun getName(): String {
        return "craftingView"
    }

    override fun show(player: UUID) {
        if (player !in playerMap) {
            val sound = page.modifier<String>("resultSound")
                ?.let {
                    return@let Sound.sound(
                        Key.key(it),
                        Sound.Source.AMBIENT,
                        page.modifierOr("resultSoundVolume", 1f),
                        page.modifierOr("resultSoundPitch", 1f),
                    )
                }

            val view = IFCraftingPlayerView(
                player,
                page.modifierOr("recipeOptions", options(3 x 9)),
                page.modifier("recipeSlots")
                    ?: throw IllegalStateException("Recipe slots mapping is not defined"),
                page.modifier<Map<Int, Int>>("resultSlots")?.entries?.associate { (k, v) -> v to k }
                    ?: throw IllegalStateException("Recipe's result slots mapping is not defined"),
                sound,
                page.modifierOr("blockedSlots", linkedSetOf()),
                TextUtils.parse(page.info.title),
                page.info.size,
            )

            val size = page.info.size x 9
            for (item in page.info.items) {
                val slot = item.modifier<Int>("slot")?.let { size.slot(it) } ?: continue
                val pane = StaticPane(slot.y, slot.x, 1, 1)
                val stack = ToughWiki.getItemFactory().produce(Bukkit.getPlayer(player), item)
                val mapper = ObjectMapper(YAMLFactory())

                val actionMap: MutableMap<TriggerType, Action> =
                    EnumMap(me.theseems.toughwiki.api.view.TriggerType::class.java)
                if (item.modifiers.containsKey("leftClickAction")) {
                    val type = TriggerType.LEFT_MOUSE_BUTTON
                    val action = ToughWiki.getActionFactory()
                        .produce(
                            type,
                            item.modifiers["leftClickAction"] as ObjectNode?,
                        )
                    if (action != null) {
                        actionMap[type] = action
                    }
                }
                if (item.modifiers.containsKey("rightClickAction")) {
                    val type = TriggerType.RIGHT_MOUSE_BUTTON
                    val action = ToughWiki.getActionFactory()
                        .produce(
                            type,
                            item.modifiers["rightClickAction"] as ObjectNode?,
                        )
                    if (action != null) {
                        actionMap[type] = action
                    }
                }
                if (actionMap.isEmpty()) {
                    for (value in TriggerType.values()) {
                        val produced = ToughWiki.getActionFactory().produce(value, mapper.valueToTree(item)) ?: continue
                        actionMap[value] = produced
                    }
                }

                pane.addItem(
                    GuiItem(stack) {
                        try {
                            val type: TriggerType
                            when (it.click) {
                                ClickType.LEFT -> type = TriggerType.LEFT_MOUSE_BUTTON
                                ClickType.RIGHT -> type = TriggerType.RIGHT_MOUSE_BUTTON
                                else -> {
                                    it.isCancelled = true
                                    return@GuiItem
                                }
                            }
                            if (!actionMap.containsKey(type)) {
                                it.isCancelled = true
                                return@GuiItem
                            }

                            val action = actionMap[type]

                            val container = SimpleComponentContainer()
                            container.storeValue("playerView", view)
                            container.storeValue("slot", slot)
                            container.storeValue("event", it)

                            ToughWikiAPI.getInstance().actionEmitter.emit(
                                action,
                                object : ActionSender {
                                    override fun getItemConfig() = item
                                    override fun getView() = this@CraftingView
                                    override fun getContainer() = container
                                },
                            )
                        } finally {
                            it.isCancelled = true
                        }
                    },
                    0,
                    0,
                )
                view.chestGui.addPane(pane)
            }

            playerMap[player] = view
        }

        playerMap[player]!!.show()
    }

    override fun dispose(player: UUID) {
        val view = playerMap[player] ?: return
        view.dispose()
        playerMap.remove(player)
    }

    override fun dispose() {
        playerMap.values.forEach { it.dispose() }
        HandlerList.unregisterAll(this)
    }

    // i reckon we don't need to add support for this, so let's make it virtually read-only
    override fun getGlobalContext() = ObjectNode(ObjectMapper(YAMLFactory()).nodeFactory)
    override fun getContext(player: UUID) = globalContext
}
