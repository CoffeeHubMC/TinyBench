package me.theseems.tinybench

import me.theseems.tinybench.recipe.RecipeOptions
import me.theseems.tinybench.recipe.RecipeOptionsBlockType

infix fun Int.x(width: Int): RecipeOptions.SizeOptions = SimpleSizeOptions(this, width)

data class SimpleSizeOptions(override val height: Int, override val width: Int) : RecipeOptions.SizeOptions

data class SimpleBlockOptions(
    override val blockType: RecipeOptionsBlockType,
    override val optionsBlockList: Collection<String>
) : RecipeOptions.BlockOptions

data class SimpleRecipeOptions(
    override val size: SimpleSizeOptions,
    override val block: SimpleBlockOptions? = null
) : RecipeOptions {
    companion object {
        fun options(size: RecipeOptions.SizeOptions, block: SimpleBlockOptions? = null) =
            SimpleRecipeOptions(SimpleSizeOptions(size.height, size.width), block)

        infix fun RecipeOptions.SizeOptions.blockingRecipes(blockList: List<String>) =
            options(SimpleSizeOptions(height, width), SimpleBlockOptions(RecipeOptionsBlockType.BLOCK_LIST, blockList))

        infix fun RecipeOptions.SizeOptions.allowingOnlyRecipes(blockList: List<String>) =
            options(SimpleSizeOptions(height, width), SimpleBlockOptions(RecipeOptionsBlockType.ALLOW_LIST, blockList))
    }
}
