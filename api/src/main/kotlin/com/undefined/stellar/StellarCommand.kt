package com.undefined.stellar

import com.undefined.stellar.exception.UnsupportedVersionException
import com.undefined.stellar.manager.CommandManager
import com.undefined.stellar.util.NMSVersion
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.ApiStatus

class StellarCommand(name: String, vararg aliases: String = arrayOf()) : AbstractStellarCommand<StellarCommand>(name) {

    constructor(name: String, aliases: List<String>) : this(name, aliases = aliases.toTypedArray())

    init {
        this.aliases.addAll(aliases)
    }

    override fun register(plugin: JavaPlugin) {
        StellarCommands.commands.add(this)
        CommandManager.initialize(plugin)
        val registrar = CommandManager.registrars[NMSVersion.version] ?: throw UnsupportedVersionException()
        registrar.register(this)
        for (execution in this.registerExecutions) execution()
    }

    companion object {
        @ApiStatus.Internal
        fun parseAndReturnCancelled(sender: CommandSender, input: String): Boolean {
            val registrar = CommandManager.registrars[NMSVersion.version] ?: throw UnsupportedVersionException()
            return registrar.handleCommandFailure(sender, input)
        }
    }

}