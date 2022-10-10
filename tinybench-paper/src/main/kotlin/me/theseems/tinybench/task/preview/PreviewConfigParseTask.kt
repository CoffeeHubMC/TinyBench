package me.theseems.tinybench.task.preview

import me.theseems.tinybench.TinyBench
import me.theseems.tinybench.config.PreviewConfig
import me.theseems.toughwiki.impl.bootstrap.BootstrapTask
import me.theseems.toughwiki.impl.bootstrap.Phase
import me.theseems.toughwiki.jackson.databind.ObjectMapper
import me.theseems.toughwiki.jackson.dataformat.yaml.YAMLFactory
import me.theseems.toughwiki.jackson.dataformat.yaml.YAMLGenerator
import java.io.File
import java.util.logging.Logger

class PreviewConfigParseTask(private val file: File) : BootstrapTask("parsePreviewConfig", Phase.CONFIG) {
    override fun run(logger: Logger) {
        val mapper = ObjectMapper(YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER))
        if (!file.exists()) {
            file.createNewFile()
            mapper.writeValue(file, PreviewConfig(mapOf()))
        }

        mapper.readValue(file, PreviewConfig::class.java)
            .let { TinyBench.previewConfig = it }
    }
}
