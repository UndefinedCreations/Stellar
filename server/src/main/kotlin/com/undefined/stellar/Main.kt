package com.undefined.stellar

import com.undefined.stellar.kotlin.KotlinBaseStellarCommand
import com.undefined.stellar.kotlin.advancedListArgument
import com.undefined.stellar.kotlin.asyncExecution
import com.undefined.stellar.kotlin.listArgument
import com.undefined.stellar.kotlin.onlinePlayersArgument
import kotlinx.coroutines.Dispatchers
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

object MenuCommands : KotlinBaseStellarCommand("menu", "menu.admin", listOf("menus")) {
    override fun setup(): StellarCommand = kotlinCommand {
        "open" {
            onlinePlayersArgument("player") {
                listArgument(
                    "menu",
                    { listOf("abc", "bcd", "cde") },
                    parse = { id -> id },
                    context = Dispatchers.IO,
                    converter = { settings -> settings }
                ) {
                    asyncExecution<CommandSender> {
                        val player: Player by args
                        val menu: String by args

                        player.sendMessage(menu)
                    }
                }
            }
        }
    }
}

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarConfig.setPlugin(this)

        MenuCommands.register(this)
    }

}