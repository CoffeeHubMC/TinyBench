package me.theseems.tinybench

fun grid(size: RecipeOptions.SizeOptions, pairs: List<Pair<Slot, Item>>) =
    mapOf(*pairs.toTypedArray()).toGridContainer(size)

fun grid(size: RecipeOptions.SizeOptions, vararg pairs: Pair<Slot, Item>) =
    mapOf(*pairs).toGridContainer(size)

fun RecipeOptions.SizeOptions.slot(slot: Int) = Slot(slot / width, slot - slot / width * width)

fun RecipeOptions.SizeOptions.slot(slot: Slot) = slot.x * width + slot.y
