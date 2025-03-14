package com.undefined.stellar

import org.bukkit.plugin.java.JavaPlugin

abstract class BaseStellarCommand(val name: String, val permission: String = "", val aliases: List<String> = listOf()) {

    private var hasBeenRegistered = false

    val command: StellarCommand by lazy {
        setup().apply { for (argument in arguments()) addArgument(argument.fullArgument) }
    }

    abstract fun setup(): StellarCommand
    open fun arguments(): List<StellarArgument> = listOf()

    fun createCommand(init: StellarCommand.() -> Unit): StellarCommand {
        val command = StellarCommand(name, permission, aliases)
        command.init()
        return command
    }

    fun register(plugin: JavaPlugin) {
        if (hasBeenRegistered) return
        hasBeenRegistered = true
        command.register(plugin)
    }

}