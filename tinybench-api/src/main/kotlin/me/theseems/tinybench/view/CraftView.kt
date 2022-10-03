package me.theseems.tinybench.view

import com.fasterxml.jackson.databind.JsonNode
import java.util.*

interface CraftView {
    val context: JsonNode

    fun show(playerUUID: UUID)
    fun dispose(playerUUID: UUID)
    fun dispose()
}
