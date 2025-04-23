package com.undefined.stellar

import com.undefined.stellar.argument.basic.StringType
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("message")
            .addAliases("msg", "tell")
            .addStringArgument("string", StringType.PHRASE)
            .addRequirement("example.user.message")
            .addExecution<Player> {
                val target: Player by args
                val message: String by args
                target.sendMessage(message)
            }
            .register()
    }

}