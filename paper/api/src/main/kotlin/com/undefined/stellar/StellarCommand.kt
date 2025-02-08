package com.undefined.stellar

import com.undefined.stellar.data.events.StellarCommandRegisterEvent
import com.undefined.stellar.data.requirement.PermissionStellarRequirement
import com.undefined.stellar.exception.UnsupportedVersionException
import com.undefined.stellar.manager.CommandManager
import com.undefined.stellar.util.NMSVersion
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.ApiStatus

class StellarCommand(name: String, permissions: List<String> = listOf(), aliases: List<String> = listOf()) : AbstractStellarCommand<StellarCommand>(name, aliases = aliases) {

    constructor(name: String, aliases: List<String>) : this(name, permissions = listOf(), aliases = aliases)
    constructor(name: String, permission: String, aliases: List<String> = listOf()) : this(name, listOf(permission), aliases)

    init {
        this.permissionRequirements.addAll(permissions.map { PermissionStellarRequirement(1, it) })
    }

    private var registered = false

    override fun register(plugin: JavaPlugin) {
        if (registered) return
        registered = true
        StellarCommands.commands.add(this)
        CommandManager.initialize(plugin)
        val registrar = (CommandManager.registrars[NMSVersion.version] ?: throw UnsupportedVersionException()).objectInstance
        registrar?.register(this, plugin)
        for (execution in this.registerExecutions) execution()
        Bukkit.getPluginManager().callEvent(StellarCommandRegisterEvent(this))
    }

    companion object {
        @ApiStatus.Internal
        fun parseAndReturnCancelled(sender: CommandSender, input: String): Boolean {
            val registrar = (CommandManager.registrars[NMSVersion.version] ?: throw UnsupportedVersionException()).objectInstance
            return registrar?.handleCommandFailure(sender, input) ?: false
        }
    }

}