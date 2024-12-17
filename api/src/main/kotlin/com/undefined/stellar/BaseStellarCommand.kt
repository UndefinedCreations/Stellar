package com.undefined.stellar

import org.bukkit.plugin.java.JavaPlugin

abstract class BaseStellarCommand(val name: String, val description: String = "", val permissions: List<String> = listOf()) {

    var hasInitializedArguments = false
        private set
    private var hasBeenRegistered = false

    val command: StellarCommand by lazy {
        setup().apply { addRequirements(*permissions.toTypedArray()) }
    }

    private fun initializeArguments() {
        if (hasInitializedArguments) return
        hasInitializedArguments = true
        for (argument in arguments()) command.addArgument(argument.getFullArgument())
    }

    abstract fun setup(): StellarCommand
    open fun arguments(): List<StellarArgument> = listOf()

    fun createCommand(init: StellarCommand.() -> Unit): StellarCommand {
        val command = StellarCommand(name, permissions)
        command.setDescription(description)
        command.init()
        return command
    }

    fun getFullCommand(): StellarCommand {
        initializeArguments()
        return command
    }

    fun register(plugin: JavaPlugin) {
        if (hasBeenRegistered) return
        getFullCommand().register(plugin)
        hasBeenRegistered = true
    }

}
