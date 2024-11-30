package com.undefined.stellar

import com.undefined.stellar.exception.UnsupportedVersionException
import com.undefined.stellar.listener.StellarListener
import com.undefined.stellar.manager.CommandManager
import com.undefined.stellar.util.NMSVersion
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

val registrars = mapOf(
    "1.20.6" to com.undefined.stellar.v1_20_6.BrigadierCommandRegistrar
)

class StellarCommand(name: String, description: String = "", vararg aliases: String = arrayOf()) : AbstractStellarCommand<StellarCommand>(name, description) {

    constructor(name: String, vararg aliases: String) : this(name, "", aliases = aliases)
    constructor(name: String, aliases: List<String>) : this(name, "", aliases = aliases.toTypedArray())

    init {
        this.aliases.addAll(aliases)
    }

    override fun register(plugin: JavaPlugin) {
        val registrar = registrars[NMSVersion.version] ?: throw UnsupportedVersionException()
        registrar.register(this)
        CommandManager.initialize(plugin)
        StellarCommands.commands.add(this)
    }

    companion object {
        fun parseAndReturnCancelled(sender: CommandSender, input: String): Boolean {
            val registrar = registrars[NMSVersion.version] ?: throw UnsupportedVersionException()
            return registrar.parseAndReturnCancelled(sender, input)
        }
    }

}