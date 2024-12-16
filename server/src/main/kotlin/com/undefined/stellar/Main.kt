package com.undefined.stellar

import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        val main = StellarCommand("test")
        main.addLocationArgument("location")
            .addRequirements(4)
            .addOnlinePlayersArgument("string")
            .addExecution<Player> {
                sender.sendMessage(this.getArgument<Player>("string").name)
            }
            .onRegister {
                println("This runs on register")
            }
            .register(this)
    }

}