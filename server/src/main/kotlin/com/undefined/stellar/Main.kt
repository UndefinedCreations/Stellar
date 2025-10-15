package com.undefined.stellar

import com.undefined.stellar.kotlin.KotlinBaseStellarCommand
import com.undefined.stellar.kotlin.asyncExecution
import com.undefined.stellar.kotlin.listArgument
import com.undefined.stellar.kotlin.setScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

object MenuCommands : KotlinBaseStellarCommand("menu", "menu.admin", listOf("menus")) {
    override fun setup(): StellarCommand = kotlinCommand {
        "open" {
            listArgument(
                "menu",
                { listOf("abc", "bcd", "cde") },
                parse = { id -> id },
                converter = { settings -> settings }
            ) {
                asyncExecution<CommandSender> {
                    val menu: String by args

                    sender.sendMessage(menu)
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