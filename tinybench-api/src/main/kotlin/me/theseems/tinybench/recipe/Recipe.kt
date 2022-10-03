package me.theseems.tinybench.recipe

import me.theseems.tinybench.item.ItemMapping

interface Recipe {
    val name: String
    fun produce(items: ItemMapping): ItemMapping
}
