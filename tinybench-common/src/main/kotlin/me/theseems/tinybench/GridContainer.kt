package me.theseems.tinybench

import me.theseems.tinybench.item.Item
import me.theseems.tinybench.item.ItemMapping
import me.theseems.tinybench.recipe.RecipeOptions

fun Map<Slot, Item>.toGridContainer(height: Int, width: Int): GridContainer {
    val container = GridContainer(height, width)
    forEach { (slot, item) ->
        container.set(slot.x, slot.y, item)
    }
    return container
}

fun Map<Slot, Item>.toSingularGridContainer(height: Int, width: Int): GridContainer {
    val container = GridContainer(height, width)
    forEach { (slot, item) ->
        val singularItem = item.clone()
        singularItem.amount = 1

        container.set(slot.x, slot.y, singularItem)
    }
    return container
}

fun Map<Slot, Item>.toGridContainer(options: RecipeOptions.SizeOptions) = toGridContainer(options.height, options.width)

fun Map<Slot, Item>.toSingularGridContainer(options: RecipeOptions.SizeOptions) =
    toSingularGridContainer(options.height, options.width)

class GridContainer(val height: Int, val width: Int) {
    val content: Array<Array<Item?>> = Array(height) { Array(width) { null } }

    fun set(x: Int, y: Int, item: Item) {
        if (x > height || x < 0) {
            throw IllegalStateException("X is not within the valid range: [0, $height)")
        }
        if (y > width || y < 0) {
            throw IllegalStateException("Y is not within the valid range: [0, $width)")
        }
        content[x][y] = item
    }

    fun get(x: Int, y: Int): Item? {
        if (x > height || x < 0) {
            return null
        }
        if (y > width || y < 0) {
            return null
        }
        return content[x][y]
    }

    fun toMapping(): ItemMapping {
        val mapping = mutableMapOf<Slot, Item>()
        for (i in 0 until height) {
            for (j in 0 until width) {
                if (get(i, j) != null) {
                    mapping[Slot(i, j)] = content[i][j]!!
                }
            }
        }

        return mapping
    }

    fun stripAmount(): Pair<GridContainer, Map<Slot, Int>> {
        val grid = GridContainer(height, width)
        val mapping = mutableMapOf<Slot, Int>()

        for (i in 0 until height) {
            for (j in 0 until width) {
                val current = content[i][j] ?: continue

                val amount = current.amount

                val cloned = current.clone()
                cloned.amount = 1

                mapping[Slot(i, j)] = amount
                grid.set(i, j, cloned)
            }
        }

        return Pair(grid, mapping)
    }

    override fun toString(): String {
        return "GridContainer(height=$height, width=$width, content=${content.contentToString()})"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GridContainer

        if (!content.contentDeepEquals(other.content)) return false

        return true
    }

    override fun hashCode(): Int {
        return content.contentDeepHashCode()
    }
}
