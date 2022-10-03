package me.theseems.tinybench.config;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.JsonNode;
import me.theseems.tinybench.SimpleSizeOptions;

import java.util.HashMap;
import java.util.Map;

public class RecipeConfig {
    private String type;
    private SimpleSizeOptions size;
    @JsonUnwrapped
    private Map<String, JsonNode> properties;

    @JsonAnyGetter
    public Map<String, JsonNode> getProperties() {
        return properties;
    }

    @JsonAnySetter
    public void add(String key, JsonNode value) {
        if (properties == null) {
            properties = new HashMap<>();
        }
        properties.put(key, value);
    }

    public String getType() {
        return type;
    }

    public SimpleSizeOptions getSize() {
        return size;
    }
}
