package me.theseems.tinybench.command

import me.theseems.tinybench.BuildConstants
import me.theseems.toughwiki.paper.commands.CommandContainer
import me.theseems.toughwiki.utils.TextUtils
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.command.CommandSender

open class TinyBenchCommandContainer : CommandContainer() {
    override fun showBanner(sender: CommandSender?) {
        sender?.sendMessage(
            LegacyComponentSerializer.legacyAmpersand()
                .deserialize("&5&lTinyBench &rby TheSeems<me@theseems.ru> &7v${BuildConstants.VERSION}")
        )
    }

    override fun showHelp(commandLabel: String, sender: CommandSender) {
        showBanner(sender)
        for (value in subCommandMap.values) {
            if (sender.hasPermission(value.permission)) {
                sender.sendMessage(
                    TextUtils
                        .parse("&5/" + commandLabel + " " + value.label + " &7- &f" + value.description)
                        .clickEvent(ClickEvent.suggestCommand("/" + commandLabel + " " + value.label))
                )
            }
        }
    }
}
