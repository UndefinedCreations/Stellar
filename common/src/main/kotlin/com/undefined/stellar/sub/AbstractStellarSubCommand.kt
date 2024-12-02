package com.undefined.stellar.sub

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.data.argument.CommandContext
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

abstract class AbstractStellarSubCommand<T>(val parent: AbstractStellarCommand<*>, name: String) : AbstractStellarCommand<T>(name) {
    override fun getBase(): AbstractStellarCommand<*> = parent.getBase()
    override fun register(plugin: JavaPlugin) = parent.register(plugin)
}