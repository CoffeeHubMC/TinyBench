package me.theseems.tinybench.util

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import me.theseems.tinybench.config.RecipeConfig
import me.theseems.toughwiki.api.WikiPage
import me.theseems.toughwiki.api.WikiPageItemConfig

inline fun <reified T> WikiPage.modifier(name: String, defaultValue: T? = null): T? {
    val modifier = info.modifiers?.get(name) ?: return defaultValue
    return ObjectMapper(YAMLFactory()).registerKotlinModule()
        .readValue(modifier.toString(), object : TypeReference<T>() {}) ?: defaultValue
}

inline fun <reified T> WikiPage.modifierOr(name: String, defaultValue: T): T {
    val modifier = info.modifiers?.get(name) ?: return defaultValue
    return ObjectMapper(YAMLFactory()).registerKotlinModule()
        .readValue(modifier.toString(), object : TypeReference<T>() {})
        ?: defaultValue
}

inline fun <reified T> WikiPageItemConfig.modifier(name: String, defaultValue: T? = null): T? {
    val modifier = modifiers?.get(name) ?: return defaultValue
    return ObjectMapper(YAMLFactory()).registerKotlinModule()
        .readValue(modifier.toString(), object : TypeReference<T>() {})
        ?: defaultValue
}

inline fun <reified T> WikiPageItemConfig.modifierOr(name: String, defaultValue: T): T {
    val modifier = modifiers?.get(name) ?: return defaultValue
    return ObjectMapper(YAMLFactory()).registerKotlinModule()
        .readValue(modifier.toString(), object : TypeReference<T>() {})
        ?: defaultValue
}

inline fun <reified T> RecipeConfig.modifier(name: String, defaultValue: T? = null): T? {
    val modifier = properties[name] ?: return defaultValue
    return ObjectMapper(YAMLFactory()).registerKotlinModule()
        .readValue(modifier.toString(), object : TypeReference<T>() {})
        ?: defaultValue
}

inline fun <reified T> RecipeConfig.modifierOr(name: String, defaultValue: T): T {
    val modifier = properties[name] ?: return defaultValue
    return ObjectMapper(YAMLFactory()).registerKotlinModule()
        .readValue(modifier.toString(), object : TypeReference<T>() {})
        ?: defaultValue
}
