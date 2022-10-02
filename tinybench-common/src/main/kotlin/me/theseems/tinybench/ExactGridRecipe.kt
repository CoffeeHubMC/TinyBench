package me.theseems.tinybench

class ExactGridRecipe(
    override val name: String,
    val source: GridContainer,
    private val target: GridContainer
) : Recipe {
    override fun produce(items: ItemMapping): ItemMapping {
        if (items.toGridContainer(source.height, source.width) != source) {
            return emptyMap()
        }
        return target.toMapping()
    }
}
