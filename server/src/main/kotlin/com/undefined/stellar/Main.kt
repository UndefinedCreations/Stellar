package com.undefined.stellar

import org.bukkit.block.BlockType
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        val main = StellarCommand("test", description = "this is a description", "othertest")
        main.addBlockTypeArgument("type")
            .addExecution<Player> {
                source.sendMessage(getArgument<BlockType>("type").key.toString())
            }
            .register(this)
    }

}