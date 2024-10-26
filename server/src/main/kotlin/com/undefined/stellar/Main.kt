package com.undefined.stellar

import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test")
            .addAlias("othertest")
            .addRequirement("test.test.test")
            .addRequirement(5)
            .addSubCommand("test")
            .addExecute<Player> {
                sendMessage("Test!")
            }
            .addAlias("ah")
            .addSubCommand("test2")
            .addExecute<Player> {
                sendMessage("Test2!")
            }
            .addSubCommand("test3")
            .addExecute<ConsoleCommandSender> {
                sendMessage("Console!")
            }
            .register()
    }

}