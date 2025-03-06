package com.undefined.stellar

import com.undefined.stellar.argument.misc.RegistryArgument
import io.papermc.paper.registry.RegistryKey
import org.bukkit.Material
import org.bukkit.entity.Cat
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test")
            .addArgument(RegistryArgument("registry", RegistryKey.CAT_VARIANT))
            .addExecution<Player> {
                sender.sendMessage("registry: ${getArgument<Cat.Type>("registry").key}")
            }
            .register(this)
    }

}