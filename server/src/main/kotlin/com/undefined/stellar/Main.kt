package com.undefined.stellar

import com.undefined.stellar.argument.block.BlockDataArgument
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test")
            .addArgument(BlockDataArgument("data"))
            .addExecution<Player> {
                sender.sendMessage("block data: ${getArgument<BlockData>("data").createBlockState().type.name}")
            }
            .register(this)
    }

}