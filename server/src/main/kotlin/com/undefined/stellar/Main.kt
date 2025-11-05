package com.undefined.stellar

import com.undefined.stellar.kotlin.KotlinBaseStellarCommand
import com.undefined.stellar.kotlin.asyncExecution
import com.undefined.stellar.kotlin.asyncRunnable
import com.undefined.stellar.kotlin.execution
import com.undefined.stellar.kotlin.listArgument
import com.undefined.stellar.kotlin.onlinePlayersArgument
import com.undefined.stellar.kotlin.runnable
import com.undefined.stellar.kotlin.setScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import kotlin.coroutines.suspendCoroutine

object MenuCommands : KotlinBaseStellarCommand("menu", "menu.admin", listOf("menus")) {
    override fun setup(): StellarCommand = kotlinCommand {
        "open" {
            onlinePlayersArgument("player", filter = { true }) {
                asyncRunnable<CommandSender> {
                    sender.sendMessage("test")
                    true
                }
            }
        }
    }
}

class Main : JavaPlugin() {

    val scope = CoroutineScope(Dispatchers.Default)

    override fun onEnable() {
        StellarConfig.setPlugin(this)
        StellarConfig.setScope(scope)

        MenuCommands.register(this)
    }

}