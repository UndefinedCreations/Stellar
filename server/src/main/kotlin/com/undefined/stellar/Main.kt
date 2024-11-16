package com.undefined.stellar

import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    companion object {
        lateinit var INSTANCE: Main
    }

    override fun onEnable() {
        INSTANCE = this

        StellarCommand("test")
            .addGlobalFailureMessage("testing!")
            .addFailureExecution<Player> {
                sendMessage(it)
            }
            .addSubCommand("abc")
            .addSubCommand("test")
            .register(this)
    }

}