package com.undefined.stellar.sub.custom

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.sub.AbstractStellarSubCommand
import com.undefined.stellar.sub.BaseStellarSubCommand
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

data class CustomSubCommandInfo<T>(val sender: CommandSender, val input: String, val value: T)

@Suppress("UNCHECKED_CAST")
abstract class CustomSubCommand<T>(parent: AbstractStellarCommand<*>, name: String, val type: BaseStellarSubCommand<*>) : AbstractStellarSubCommand<T>(parent, name) {
    open fun getSubCommandsList(): List<BaseStellarSubCommand<*>> = listOf()
    abstract fun parse(sender: CommandSender, input: String): T
    open fun <T> execution(info: CustomSubCommandInfo<T>) {}
    open fun <T> failureExecution(info: CustomSubCommandInfo<T>) {}
    open fun requirement(): Boolean = true
    abstract fun listSuggestions(sender: CommandSender): List<String>

    override fun getBase(): AbstractStellarCommand<*> = parent.getBase()
    override fun register(plugin: JavaPlugin) = parent.register(plugin)

}