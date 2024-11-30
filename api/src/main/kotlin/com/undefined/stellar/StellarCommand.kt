package com.undefined.stellar

import com.undefined.stellar.exception.UnsupportedVersionException
import com.undefined.stellar.listener.StellarListener
import com.undefined.stellar.manager.CommandManager
import com.undefined.stellar.util.NMSVersion
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

class StellarCommand(name: String, description: String = "", vararg aliases: String = arrayOf()) : AbstractStellarCommand<StellarCommand>(name, description) {

    constructor(name: String, vararg aliases: String) : this(name, "", aliases = aliases)
    constructor(name: String, aliases: List<String>) : this(name, "", aliases = aliases.toTypedArray())

    init {
        this.aliases.addAll(aliases)
    }

    override fun register(plugin: JavaPlugin) {
        val registrar = CommandManager.registrars[NMSVersion.version] ?: throw UnsupportedVersionException()
        registrar.register(this)
        CommandManager.initialize(plugin)
        StellarCommands.commands.add(this)
    }

    companion object {
        fun parseAndReturnCancelled(sender: CommandSender, input: String): Boolean {
            val registrar = CommandManager.registrars[NMSVersion.version] ?: throw UnsupportedVersionException()
            return registrar.parseAndReturnCancelled(sender, input)
        }
    }

}