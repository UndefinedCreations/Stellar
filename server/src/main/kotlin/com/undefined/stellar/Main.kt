package com.undefined.stellar

import org.bukkit.World.Environment
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    override fun onEnable() {
        StellarCommand("test")
            .addEnumArgument<Environment>("env")
            .addExecution<Player> {
                val env = getArgument<Environment>("env")
                sender.sendMessage(env.name)
            }
            .register(this)
    }
}