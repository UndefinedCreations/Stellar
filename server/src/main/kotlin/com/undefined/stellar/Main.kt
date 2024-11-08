package com.undefined.stellar

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test")
            .addBlockPredicateSubCommand("predicate")
            .addBlockPredicateExecution<Player> { predicate ->
                sendMessage(predicate.test(world.getBlockAt(Location(world, 0.0, 64.0, 0.0))).toString())
            }
            .register()
    }

}