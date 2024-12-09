package com.undefined.stellar

import org.bukkit.entity.Cat
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        val main = StellarCommand("test", description = "this is a description", "othertest")
        main.addCatTypeArgument("type")
            .addExecution<Player> {
                source.sendMessage(getArgument<Cat.Type>("type").key.toString())
            }
            .register(this)
    }

}