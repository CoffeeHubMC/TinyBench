package me.theseems.tinybench.config;

import me.theseems.toughwiki.api.WikiPageItemConfig;

import java.util.List;

public class PreviewEntry {
    private String page;
    private String recipe;
    private List<WikiPageItemConfig> items;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public List<WikiPageItemConfig> getItems() {
        return items;
    }

    public void setItems(List<WikiPageItemConfig> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "PreviewEntry{" +
                "page='" + page + '\'' +
                ", recipe='" + recipe + '\'' +
                ", items=" + items +
                '}';
    }
}
