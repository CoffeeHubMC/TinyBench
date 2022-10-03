package me.theseems.tinybench.config;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;

public class ItemConfig {
    private String type;
    @JsonUnwrapped
    private Map<String, JsonNode> modifiers;

    @JsonAnyGetter
    public Map<String, JsonNode> getModifiers() {
        return modifiers;
    }

    @JsonAnySetter
    public void add(String key, JsonNode value) {
        if (modifiers == null) {
            modifiers = new HashMap<>();
        }
        modifiers.put(key, value);
    }

    public ItemConfig() {
    }

    public ItemConfig(String type, Map<String, JsonNode> modifiers) {
        this.type = type;
        this.modifiers = modifiers;
    }

    public String getType() {
        return type;
    }
}
