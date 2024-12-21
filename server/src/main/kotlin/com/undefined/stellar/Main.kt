package com.undefined.stellar

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test")
            .addStringArgument(name = "string")
            .addExecution<Player> {
                sender.sendMessage(getArgument<String>("string"))
            }
            .register(this)
    }

}