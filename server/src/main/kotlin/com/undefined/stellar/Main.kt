package com.undefined.stellar

import com.undefined.stellar.argument.basic.StringArgument
import com.undefined.stellar.argument.misc.RegistryArgument
import com.undefined.stellar.util.unregisterCommand
import org.bukkit.Registry
import org.bukkit.entity.Cat
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test")
            .addArgument(RegistryArgument("registry", Registry.CAT_VARIANT))
            .addExecution<Player> {
                sender.sendMessage(getArgument<Cat.Type>("registry").key.toString())
            }
            .register(this)
    }

}