package com.undefined.stellar

import org.bukkit.Instrument
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        val command = StellarCommand("test")
        command
            .addArgument("t")
            .addArguments
            .addInstrumentArgument("block")
            .addExecution<Player> {
                sender.sendMessage(getArgument<Instrument>("block").name)
            }
        command.register(this)
    }

}