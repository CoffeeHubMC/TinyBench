package me.theseems.tinybench.listener

import me.theseems.tinybench.TinyBench
import me.theseems.tinybench.event.RecipeCraftEvent
import me.theseems.tinybench.item.PhantomItem
import me.theseems.toughwiki.utils.TextUtils
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class MoneyRecipeConstraintListener : Listener {
    @EventHandler
    fun onMoneyRecipe(event: RecipeCraftEvent) {
        val moneyPhantoms = event.rewards.values
            .filter { it is PhantomItem && it.content.get("cost")?.asDouble() != null }
            .map { it as PhantomItem }
        if (moneyPhantoms.isEmpty()) {
            return
        }

        val requiredMoney = moneyPhantoms.sumOf { it.content.get("cost").asDouble() }
        val economyProvider = Bukkit.getServer().servicesManager.getRegistration(Economy::class.java)?.provider

        if (economyProvider == null) {
            TinyBench.plugin.logger.warning("No vault found but recipe output requires money withdrawal")
            event.isCancelled = true
            return
        }

        val offlinePlayer = Bukkit.getOfflinePlayer(event.playerUUID)
        if (!economyProvider.has(offlinePlayer, requiredMoney)) {
            Bukkit.getPlayer(event.playerUUID)
                ?.sendMessage(
                    TextUtils.parse(
                        "&cNot enough money." +
                            " This recipe requires you to have at least $requiredMoney in your account."
                    )
                )
            event.isCancelled = true
        }

        economyProvider.withdrawPlayer(offlinePlayer, requiredMoney)
    }
}
