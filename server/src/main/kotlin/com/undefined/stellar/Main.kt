package com.undefined.stellar

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemType
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        val main = StellarCommand("test", description = "this is a description", "othertest")
        main.addItemTypeArgument("type")
            .addExecution<Player> {
                source.sendMessage(getArgument<ItemType>("type").key.toString())
            }
            .register(this)
    }

}