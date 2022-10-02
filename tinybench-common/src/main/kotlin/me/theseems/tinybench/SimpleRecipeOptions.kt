package me.theseems.tinybench

infix fun Int.x(width: Int): RecipeOptions.SizeOptions = SimpleSizeOptions(this, width)

class SimpleSizeOptions(override val height: Int, override val width: Int) : RecipeOptions.SizeOptions

class SimpleBlockOptions(
    override val blockType: RecipeOptionsBlockType,
    override val optionsBlockList: Collection<String>
) : RecipeOptions.BlockOptions

class SimpleRecipeOptions(
    override val size: RecipeOptions.SizeOptions,
    override val block: RecipeOptions.BlockOptions? = null
) : RecipeOptions {
    companion object {
        fun options(size: RecipeOptions.SizeOptions, block: RecipeOptions.BlockOptions? = null) =
            SimpleRecipeOptions(size, block)

        infix fun RecipeOptions.SizeOptions.blockingRecipes(blockList: List<String>) =
            options(this, SimpleBlockOptions(RecipeOptionsBlockType.BLOCK_LIST, blockList))

        infix fun RecipeOptions.SizeOptions.allowingOnlyRecipes(blockList: List<String>) =
            options(this, SimpleBlockOptions(RecipeOptionsBlockType.ALLOW_LIST, blockList))
    }
}
