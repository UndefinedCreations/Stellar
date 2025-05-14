package com.undefined.stellar

import com.undefined.stellar.argument.basic.StringType
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarConfig.setPlugin(this)

//        com.undefined.lynx.LynxConfig

        StellarCommand("test")
            .addArgument("players")
            .addRunnable<Player>(true) {
                sender.sendMessage(getOrNull<Int>("page").toString())
                true
            }
            .addIntegerArgument("page")
            .register()

        StellarCommand("message")
            .addExecution { a -> a.sender.sendMessage("h") }
            .addAliases("msg", "tell")
            .addOnlinePlayersArgument("target")
            .addStringArgument("message", StringType.PHRASE)
            .addRequirement("example.user.message")
            .addExecution<Player> {
                val target: Player by args
                val message: String by args
                sender.sendMessage("${ChatColor.GREEN}You successfully sent a message to ${target.name}.")
                target.sendMessage(message)
            }
            .register()
    }

}