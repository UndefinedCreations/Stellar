package com.undefined.stellar

import com.undefined.stellar.argument.list.RegistryArgument
import org.bukkit.Material
import org.bukkit.Registry
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test")
            .addArgument(RegistryArgument("registry", Registry.MATERIAL))
            .addExecution<Player> {
                sender.sendMessage("enum: ${getArgument<Material>("registry").name}")
            }
            .register(this)
    }

}