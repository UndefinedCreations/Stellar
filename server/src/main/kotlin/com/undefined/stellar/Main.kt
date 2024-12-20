package com.undefined.stellar

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        val command = StellarCommand("test")
        command.addArgument("t")
            .addLocationArgument("block")
            .addExecution<Player> {
                sender.sendMessage(getArgument<Location>("block").toVector().toString())
            }
        command.register(this)
    }

}