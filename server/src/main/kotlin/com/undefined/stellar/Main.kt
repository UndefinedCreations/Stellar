package com.undefined.stellar

import com.undefined.stellar.argument.LiteralArgument
import com.undefined.stellar.argument.basic.StringArgument
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test")
            .addArgument(StringArgument("string"))
            .addSuggestion("test", "a!")
            .addArgument(LiteralArgument("b"))
            .addExecution<Player> {
                sender.sendMessage("string: ${getArgument<String>("string")}")
            }
            .register(this)
    }

}