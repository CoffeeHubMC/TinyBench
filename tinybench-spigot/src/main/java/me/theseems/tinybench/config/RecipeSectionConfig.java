package me.theseems.tinybench.config;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.HashMap;
import java.util.Map;

public class RecipeSectionConfig {
    @JsonUnwrapped
    private Map<String, RecipeConfig> recipes;

    @JsonAnyGetter
    public Map<String, RecipeConfig> getPages() {
        return recipes;
    }

    @JsonAnySetter
    public void add(String key, RecipeConfig value) {
        if (recipes == null) {
            recipes = new HashMap<>();
        }
        recipes.put(key, value);
    }

    public Map<String, RecipeConfig> getRecipes() {
        return recipes;
    }
}
