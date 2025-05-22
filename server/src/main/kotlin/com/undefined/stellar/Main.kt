package com.undefined.stellar

import com.undefined.stellar.argument.basic.StringType
import com.undefined.stellar.argument.misc.UUIDArgument
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.UUID

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarConfig.setPlugin(this)

        StellarCommand("Test")
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