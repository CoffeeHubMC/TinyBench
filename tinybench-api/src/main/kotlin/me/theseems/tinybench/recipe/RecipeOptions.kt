package me.theseems.tinybench.recipe

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

        fun product(): Int { // i hope it's gonna be int))
            return height * width
        }
    }

    val block: BlockOptions?
    val size: SizeOptions
}
