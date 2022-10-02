package me.theseems.tinybench

interface RecipeOptions {
    interface BlockOptions {
        val blockType: RecipeOptionsBlockType
        val optionsBlockList: Collection<String>

        fun contains(recipe: Recipe) = when (blockType) {
            RecipeOptionsBlockType.BLOCK_LIST -> recipe.name in optionsBlockList
            RecipeOptionsBlockType.ALLOW_LIST -> recipe.name !in optionsBlockList
        }
    }

    interface SizeOptions {
        val height: Int
        val width: Int
    }

    val block: BlockOptions?
    val size: SizeOptions
}
