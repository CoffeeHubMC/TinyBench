package me.theseems.tinybench

interface Recipe {
    val name: String
    fun produce(items: ItemMapping): ItemMapping
}
