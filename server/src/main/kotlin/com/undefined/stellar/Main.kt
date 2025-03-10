package com.undefined.stellar

import com.undefined.stellar.argument.list.OnlinePlayersArgument
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test")
            .addArgument(OnlinePlayersArgument("test"))
            .addExecution<Player> {
                sender.sendMessage(getArgument<Player>("test").name)
            }
//            .addArgument(RegistryArgument("registry", Registry.CAT_VARIANT))
//            .addExecution<Player> {
//                sender.sendMessage(getArgument<Cat.Type>("registry").key.toString())
//            }
            .register(this)
    }

}