package me.theseems.tinybench.task

import me.theseems.tinybench.TinyBench
import me.theseems.tinybench.command.TinyBenchCommand
import me.theseems.toughwiki.impl.bootstrap.BootstrapTask
import me.theseems.toughwiki.impl.bootstrap.Phase
import java.util.logging.Logger

class CommandRegisterTask : BootstrapTask("registerCommands", Phase.PRE_CONFIG) {
    override fun run(logger: Logger) {
        TinyBench.plugin.getCommand("tinybench")?.setExecutor(TinyBenchCommand())
    }
}
