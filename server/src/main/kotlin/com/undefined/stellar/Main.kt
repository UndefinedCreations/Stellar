package com.undefined.stellar

import com.undefined.stellar.command.register
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test")
            .addAlias("othertest")
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