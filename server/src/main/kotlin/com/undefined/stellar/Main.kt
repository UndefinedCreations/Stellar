package com.undefined.stellar

import org.bukkit.GameEvent
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        val main = StellarCommand("test", description = "this is a description", "othertest")
        main.addGameEventArgument("event")
            .addExecution<Player> {
                source.sendMessage(getArgument<GameEvent>("event").key.toString())
            }
            .register(this)
    }

}