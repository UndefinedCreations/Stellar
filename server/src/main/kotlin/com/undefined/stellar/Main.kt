package com.undefined.stellar

import org.bukkit.entity.Frog
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        val main = StellarCommand("test", description = "this is a description", "othertest")
        main.addFrogVariantArgument("type")
            .addExecution<Player> {
                source.sendMessage(getArgument<Frog.Variant>("type").key.toString())
            }
            .register(this)
    }

}