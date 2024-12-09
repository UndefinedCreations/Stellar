package com.undefined.stellar

import org.bukkit.entity.Player
import org.bukkit.generator.structure.StructureType
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        val main = StellarCommand("test", description = "this is a description", "othertest")
        main.addStructureTypeArgument("type")
            .addExecution<Player> {
                source.sendMessage(getArgument<StructureType>("type").key.toString())
            }
            .register(this)
    }

}