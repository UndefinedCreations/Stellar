package com.undefined.stellar

import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("set-time")
            .addTimeArgument("time")
            .addExecution<Player> {
                val time = getArgument<Long>("time")
                sender.world.time = time
            }
            .register(this)
    }

}