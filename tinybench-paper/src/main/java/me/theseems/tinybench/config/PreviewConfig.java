package me.theseems.tinybench.config;


import me.theseems.toughwiki.jackson.annotation.JsonAnyGetter;
import me.theseems.toughwiki.jackson.annotation.JsonAnySetter;
import me.theseems.toughwiki.jackson.annotation.JsonUnwrapped;

import java.util.HashMap;
import java.util.Map;

public class PreviewConfig {
    @JsonUnwrapped
    private Map<String, PreviewEntry> entries;

    public PreviewConfig() {
    }

    public PreviewConfig(Map<String, PreviewEntry> entries) {
        this.entries = entries;
    }

    @JsonAnyGetter
    public Map<String, PreviewEntry> getEntries() {
        return entries;
    }

    @JsonAnySetter
    public void add(String key, PreviewEntry value) {
        if (entries == null) {
            entries = new HashMap<>();
        }
        entries.put(key, value);
    }
}
