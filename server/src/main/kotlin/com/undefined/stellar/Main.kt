package com.undefined.stellar

import com.undefined.stellar.argument.basic.StringArgument
import com.undefined.stellar.util.unregisterCommand
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        unregisterCommand("op")
        StellarCommand("test")
            .addArgument(StringArgument("args"))
            .register(this)
    }

}