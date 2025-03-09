package com.undefined.stellar

import com.undefined.stellar.argument.basic.StringArgument
import com.undefined.stellar.argument.phrase.PhraseArgument
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test")
            .addArgument(StringArgument("args"))
            .register(this)
    }

}