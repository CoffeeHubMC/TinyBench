package me.theseems.tinybench.task

import me.theseems.tinybench.SimpleRecipeManager
import me.theseems.tinybench.TinyBenchAPI
import me.theseems.toughwiki.impl.bootstrap.BootstrapTask
import me.theseems.toughwiki.impl.bootstrap.Phase
import java.util.logging.Logger

class APIInitTask : BootstrapTask("initAPI", Phase.PRE_CONFIG) {
    override fun run(logger: Logger?) {
        TinyBenchAPI.initialize(SimpleRecipeManager())
    }
}
