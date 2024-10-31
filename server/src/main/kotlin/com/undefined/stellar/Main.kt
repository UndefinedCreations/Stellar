package com.undefined.stellar

import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test")
            .addBlockSubCommand("block")
            .addBlockExecution<Player> { type ->
                sendMessage(type.name)
//                sendBlockChange(state.location, Bukkit.createBlockData(Material.DIAMOND_BLOCK))
            }
            .register()
    }

}