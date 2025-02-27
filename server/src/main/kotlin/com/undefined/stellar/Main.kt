package com.undefined.stellar

import com.undefined.stellar.util.unregisterCommand
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        unregisterCommand("enchant", this)
        StellarCommand("enchant")
            .addArgument("a")
            .addExecution<Player> {
                sender.sendRichMessage("hi!")
            }
            .register(this)
    }

}