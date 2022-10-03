package me.theseems.tinybench.item

interface Item {
    var amount: Int

    fun clone(): Item
}
