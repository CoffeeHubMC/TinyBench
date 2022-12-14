package me.theseems.tinybench.command

import me.theseems.tinybench.TinyBench
import me.theseems.toughwiki.ToughWiki
import me.theseems.toughwiki.impl.bootstrap.Phase
import me.theseems.toughwiki.paper.commands.SubCommand
import me.theseems.toughwiki.utils.TextUtils
import org.bukkit.command.CommandSender

class ReloadSubCommand : SubCommand {
    override fun getLabel(): String {
        return "reload"
    }

    override fun getDescription(): String {
        return "reloads configuration"
    }

    override fun getPermission(): String {
        return "tinybench.command.reload"
    }

    override fun execute(sender: CommandSender?, args: Array<out String>?) {
        TinyBench.bootstrap.execute(Phase.SHUTDOWN, Phase.CONFIG, Phase.POST_CONFIG)
        ToughWiki.getBootstrap().execute(Phase.SHUTDOWN, Phase.CONFIG, Phase.POST_CONFIG)
        sender?.sendMessage(TextUtils.parse("&5Configuration reloaded"))
    }
}
