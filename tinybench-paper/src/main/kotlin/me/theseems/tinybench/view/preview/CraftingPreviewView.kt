package me.theseems.tinybench.view.preview

import java.util.UUID
import me.theseems.tinybench.exact.ExactGridRecipe
import me.theseems.tinybench.item.IconItem
import me.theseems.tinybench.recipe.Recipe
import me.theseems.tinybench.slot
import me.theseems.tinybench.util.modifier
import me.theseems.tinybench.x
import me.theseems.toughwiki.ToughWiki
import me.theseems.toughwiki.api.ToughWikiAPI
import me.theseems.toughwiki.api.WikiPage
import me.theseems.toughwiki.api.WikiPageItemConfig
import me.theseems.toughwiki.api.component.ComponentContainer
import me.theseems.toughwiki.api.view.Action
import me.theseems.toughwiki.api.view.ActionSender
import me.theseems.toughwiki.api.view.TriggerType
import me.theseems.toughwiki.api.view.WikiPageView
import me.theseems.toughwiki.impl.component.SimpleComponentContainer
import me.theseems.toughwiki.inventoryframework.adventuresupport.ComponentHolder
import me.theseems.toughwiki.inventoryframework.gui.GuiItem
import me.theseems.toughwiki.inventoryframework.gui.type.ChestGui
import me.theseems.toughwiki.inventoryframework.pane.StaticPane
import me.theseems.toughwiki.jackson.databind.ObjectMapper
import me.theseems.toughwiki.jackson.databind.node.ObjectNode
import me.theseems.toughwiki.jackson.dataformat.yaml.YAMLFactory
import me.theseems.toughwiki.utils.TextUtils
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType

class CraftingPreviewView(
    private val page: WikiPage,
    private val recipe: Recipe,
    private val additionalItems: List<WikiPageItemConfig>,
) : Listener, WikiPageView {
    private val view: ChestGui = makeView()

    override fun getPage() = page

    override fun getName(): String {
        return "craftingPreviewView"
    }

    override fun show(player: UUID) {
        Bukkit.getPlayer(player)?.let { view.show(it) }
    }

    override fun dispose(player: UUID) {
    }

    override fun dispose() {
    }

    private fun makeView(): ChestGui {
        val recipeSlots = page.modifier<Map<Int, Int>>("recipeSlots")?.entries?.associate { (k, v) -> v to k }
            ?: throw IllegalStateException("Recipe slots mapping is not defined")
        val resultSlots = page.modifier<Map<Int, Int>>("resultSlots")?.entries?.associate { (k, v) -> v to k }
            ?: throw IllegalStateException("Recipe's result slots mapping is not defined")

        val view = ChestGui(page.info.size, ComponentHolder.of(TextUtils.parse(page.info.title)))
        view.setOnGlobalDrag { it.isCancelled = true }
        view.setOnGlobalClick { it.isCancelled = true }

        val size = page.info.size x 9
        for (item in page.info.items.plus(additionalItems)) {
            val slot = item.modifier<Int>("slot")?.let { size.slot(it) } ?: continue
            val pane = StaticPane(slot.y, slot.x, 1, 1)
            val stack = ToughWiki.getItemFactory().produce(null, item)

            val actionMap = mutableMapOf<TriggerType, Action>()
            if ("leftClickAction" in item.modifiers) {
                ToughWiki.getActionFactory().produce(
                    TriggerType.LEFT_MOUSE_BUTTON,
                    item.modifiers["leftClickAction"] as ObjectNode?,
                )?.let { actionMap[TriggerType.LEFT_MOUSE_BUTTON] = it }
            }
            if ("rightClickAction" in item.modifiers) {
                ToughWiki.getActionFactory().produce(
                    TriggerType.RIGHT_MOUSE_BUTTON,
                    item.modifiers["rightClickAction"] as ObjectNode?,
                )?.let { actionMap[TriggerType.RIGHT_MOUSE_BUTTON] = it }
            }
            if (actionMap.isEmpty()) {
                for (value in TriggerType.values()) {
                    ToughWiki.getActionFactory().produce(
                        TriggerType.LEFT_MOUSE_BUTTON,
                        ObjectMapper(YAMLFactory()).valueToTree(item.modifiers),
                    )?.let { actionMap[value] = it }
                }
            }

            pane.addItem(
                GuiItem(stack) {
                    it.isCancelled = true

                    val container = SimpleComponentContainer()
                    container.storeValue("event", it)
                    container.storeValue("chestGui", view)
                    container.storeValue("slot", it.slot)

                    val sender = object : ActionSender {
                        override fun getItemConfig() = item
                        override fun getView(): WikiPageView = this@CraftingPreviewView
                        override fun getContainer(): ComponentContainer = container
                    }

                    when (it.click) {
                        ClickType.LEFT -> actionMap[TriggerType.LEFT_MOUSE_BUTTON]?.let { action ->
                            ToughWikiAPI.getInstance().actionEmitter.emit(action, sender)
                        }

                        ClickType.RIGHT -> actionMap[TriggerType.RIGHT_MOUSE_BUTTON]?.let { action ->
                            ToughWikiAPI.getInstance().actionEmitter.emit(action, sender)
                        }

                        else -> actionMap[TriggerType.UNKNOWN]?.let { action ->
                            ToughWikiAPI.getInstance().actionEmitter.emit(action, sender)
                        }
                    }
                },
                0,
                0,
            )
            view.addPane(pane)
        }

        if (recipe is ExactGridRecipe) {
            recipe.source.toMapping().forEach { (slot, item) ->
                if (item !is IconItem) {
                    return@forEach
                }

                val sourceSize = recipe.source.height x recipe.source.width

                val mappedSlot = recipeSlots[sourceSize.slot(slot)]?.let { size.slot(it) }
                    ?: throw IllegalStateException(
                        "No source slot to map: $slot -> ${sourceSize.slot(slot)} -> ${
                            recipeSlots[
                                size.slot(
                                    slot,
                                ),
                            ]
                        }?? ($recipeSlots)",
                    )

                val pane = StaticPane(mappedSlot.y, mappedSlot.x, 1, 1)
                pane.addItem(GuiItem(item.stack) { it.isCancelled = true }, 0, 0)
                view.addPane(pane)
            }
            recipe.target.toMapping().forEach { (slot, item) ->
                if (item !is IconItem) {
                    return@forEach
                }

                val targetSize = recipe.target.height x recipe.target.width

                val mappedSlot = resultSlots[targetSize.slot(slot)]?.let { size.slot(it) }
                    ?: throw IllegalStateException(
                        "No result slot to map: $slot -> ${targetSize.slot(slot)} -> ${
                            resultSlots[
                                size.slot(
                                    slot,
                                ),
                            ]
                        }?? ($resultSlots)",
                    )

                val pane = StaticPane(mappedSlot.y, mappedSlot.x, 1, 1)
                pane.addItem(GuiItem(item.stack) { it.isCancelled = true }, 0, 0)
                view.addPane(pane)
            }
        } else {
            throw IllegalStateException("Recipe type of '" + recipe.javaClass + "' is not supported yet")
        }

        return view
    }

    // i reckon we don't need to add support for this, so let's make it virtually read-only
    override fun getGlobalContext() = ObjectNode(ObjectMapper(YAMLFactory()).nodeFactory)
    override fun getContext(player: UUID) = globalContext
}
