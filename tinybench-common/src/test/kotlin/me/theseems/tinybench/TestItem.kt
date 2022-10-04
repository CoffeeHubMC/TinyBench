package me.theseems.tinybench

import me.theseems.tinybench.item.Item

data class TestItem(val name: String, override var amount: Int = 1) : Item {
    override fun clone() = TestItem(name, amount)
}
