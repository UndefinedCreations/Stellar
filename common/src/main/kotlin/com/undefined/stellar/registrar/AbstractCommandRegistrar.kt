package com.undefined.stellar.registrar

import com.undefined.stellar.AbstractStellarCommand
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

interface AbstractCommandRegistrar {
    fun register(command: AbstractStellarCommand<*>, plugin: JavaPlugin)
    fun unregister(name: String, plugin: JavaPlugin)
    fun handleCommandFailure(sender: CommandSender, input: String): Boolean
}