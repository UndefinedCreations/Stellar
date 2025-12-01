package com.undefined.stellar

import com.undefined.stellar.kotlin.setScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    val scope = CoroutineScope(Dispatchers.Default)

    override fun onEnable() {
        StellarConfig.setPlugin(this)
        StellarConfig.setScope(scope)

        StellarCommand("balance")
            .addAliases("bal")
            .setDescription("Check your balance")
            .setUsageText("/balance")
            .hideDefaultFailureMessages(hide = true, global = true)
            .addGlobalFailureMessage("<red>Usage: /balance")
            .addExecution<Player> {
                sender.sendMessage("<gray>Your balance: 100 euros")
            }
            .register()
    }

}