package com.undefined.stellar

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test")
            .addLocationArgument("location")
            .addStringArgument("test")
            .addExecution<Player> {
//                sender.sendMessage(getArgument<Location>(0).toVector().toString())
//                sender.sendMessage(getArgument<String>(3))
                sender.sendMessage(arguments.toList().withIndex().joinToString(", ") { "${it.index}: ${it.value.first}" })
            }
            .register(this)
    }

}