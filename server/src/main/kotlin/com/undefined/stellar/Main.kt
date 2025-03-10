package com.undefined.stellar

import com.undefined.stellar.argument.basic.StringArgument
import com.undefined.stellar.argument.basic.StringType
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test")
            .addArgument(StringArgument("test", StringType.QUOTABLE_PHRASE))
            .addExecution<Player> {
                sender.sendMessage(getArgument<String>("test"))
            }
//            .addArgument(RegistryArgument("registry", Registry.CAT_VARIANT))
//            .addExecution<Player> {
//                sender.sendMessage(getArgument<Cat.Type>("registry").key.toString())
//            }
            .register(this)
    }

}