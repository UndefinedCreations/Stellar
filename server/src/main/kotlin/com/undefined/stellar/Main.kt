package com.undefined.stellar

import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test", listOf("test"))
            .addStringArgument("test")
            .addExecution<Player> {
                sender.sendMessage(getArgument<String>("test"))
            }
            .register(this)
    }

}