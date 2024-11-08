package com.undefined.stellar

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test")
            .addItemSubCommand("item")
            .addItemExecution<Player> {
                inventory.addItem(it)
            }
            .register()
    }

}