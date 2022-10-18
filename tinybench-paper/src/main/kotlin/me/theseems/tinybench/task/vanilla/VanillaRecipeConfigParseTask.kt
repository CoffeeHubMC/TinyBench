package me.theseems.tinybench.task.vanilla

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import me.theseems.tinybench.TinyBench
import me.theseems.tinybench.config.VanillaRecipeConfig
import me.theseems.toughwiki.impl.bootstrap.BootstrapTask
import me.theseems.toughwiki.impl.bootstrap.Phase
import java.io.File
import java.util.logging.Logger

class VanillaRecipeConfigParseTask(private val file: File) : BootstrapTask("parseVanillaConfig", Phase.CONFIG) {
    override fun run(logger: Logger) {
        val mapper = ObjectMapper(YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER))
            .registerKotlinModule()
        if (!file.exists()) {
            file.createNewFile()
            mapper.writeValue(file, VanillaRecipeConfig(blockedRecipes = listOf()))
        }

        mapper.readValue<VanillaRecipeConfig>(file)
            .let { TinyBench.vanillaRecipeConfig = it }
    }
}
