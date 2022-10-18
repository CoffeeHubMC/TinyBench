package me.theseems.tinybench.recipes.leather

import me.theseems.tinybench.Slot
import me.theseems.tinybench.item.IconItem
import me.theseems.tinybench.item.Item
import me.theseems.tinybench.item.ItemMapping
import me.theseems.tinybench.item.ItemStackItem
import me.theseems.tinybench.item.PhantomItem
import me.theseems.tinybench.recipe.RecipeContainer
import me.theseems.tinybench.recipe.RecipeOptions
import me.theseems.tinybench.slot
import org.bukkit.Color
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta

class LeatherRecipeContainer : RecipeContainer<LeatherRecipe> {
    override val name: String
        get() = "leather"

    private val additional: MutableSet<Item> = mutableSetOf()

    override fun dispose() = Unit

    override fun produce(items: ItemMapping, options: RecipeOptions): RecipeContainer.RecipeResult {
        if (options.block?.contains(name) == true) {
            return RecipeContainer.RecipeResult.empty(items)
        }

        var leatherSlot: Slot? = null
        var leatherStack: ItemStack? = null
        for ((slot, item) in items) {
            if (item !is IconItem) {
                continue
            }
            val stack = item.stack
            if (stack.itemMeta !is LeatherArmorMeta) {
                continue
            }
            leatherStack = stack
            leatherSlot = slot
            break
        }
        if (leatherSlot == null) {
            return RecipeContainer.RecipeResult.empty(items)
        }

        var colorSlot: Slot? = null
        var color: Color? = null
        for ((slot, item) in items) {
            if (item !is PhantomItem) {
                continue
            }

            val rawColor = item.content.get("color")
                ?.asText(null)
                ?: continue

            colorSlot = slot
            color = Color.fromRGB(rawColor.toInt(radix = 16))
        }
        if (colorSlot == null) {
            return RecipeContainer.RecipeResult.empty(items)
        }

        val copy = leatherStack!!.clone()
        val copyMeta = copy.itemMeta as LeatherArmorMeta
        copyMeta.setColor(color)
        copy.itemMeta = copyMeta

        val result = mutableMapOf<Slot, Item>(Slot(0, 0) to ItemStackItem(copy))
        val bounding = options.size
        additional.forEachIndexed { index, item ->
            val slot = bounding.slot(index + 1)
            result[slot] = item
        }

        val leftovers = mutableMapOf<Slot, Item>()
        items.forEach { (slot, item) ->
            if (slot != leatherSlot && slot != colorSlot) {
                leftovers[slot] = item
            }
        }

        return RecipeContainer.RecipeResult(result, leftovers, LeatherRecipe("leather", additional))
    }

    override fun dispose(recipe: LeatherRecipe) {
        additional.removeAll(recipe.additional)
    }

    override fun store(recipe: LeatherRecipe) {
        additional.addAll(recipe.additional)
    }
}
