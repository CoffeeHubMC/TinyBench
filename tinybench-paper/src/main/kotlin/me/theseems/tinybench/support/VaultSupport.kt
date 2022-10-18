package me.theseems.tinybench.support

import me.theseems.tinybench.TinyBench
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit

class VaultSupport {
    var economy: Economy? = null

    fun setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            TinyBench.plugin.logger.warning("Vault support is disabled. There's no Vault plugin.")
            return
        }

        val serviceProvider = TinyBench.plugin
            .server
            .servicesManager
            .getRegistration(Economy::class.java)

        if (serviceProvider == null) {
            TinyBench.plugin.logger.warning(
                "Vault support is disabled. Service provider unexpectedly returned nothing."
            )
            return
        }

        economy = serviceProvider.provider
    }
}
