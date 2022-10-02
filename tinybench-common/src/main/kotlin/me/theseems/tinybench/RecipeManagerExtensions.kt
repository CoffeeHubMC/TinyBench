package me.theseems.tinybench

val RecipeManager.exactContainer: RecipeContainer<ExactGridRecipe>
    get() {
        return get("exact")!!
    }
