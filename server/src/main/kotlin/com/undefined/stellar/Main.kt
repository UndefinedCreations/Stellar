package com.undefined.stellar

import com.undefined.stellar.argument.LiteralArgument
import com.undefined.stellar.argument.basic.StringArgument
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        val cmd = StellarCommand("test")
            .addArgument(StringArgument("string"))
            .addArgument(LiteralArgument("a"))
            .addExecution<Player> {
                sender.sendMessage("this works!")
            }
            .register(this)
    }

}