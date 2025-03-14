package com.undefined.stellar

import org.bukkit.plugin.java.JavaPlugin
import java.util.*

/**
 * This is the base of any command, e.g. /op <target>, op would be the base command.
 *
 * @param name The name of the command.
 * @param permissions An `Iterable` of Bukkit string permissions to be applied as a requirement.
 * @param aliases An `Iterable` of name aliases.
 */
class StellarCommand(name: String, permissions: Iterable<String> = listOf(), aliases: Iterable<String> = listOf()) : AbstractStellarCommand<StellarCommand>(name) {

    val information: SortedMap<String, String> = sortedMapOf()

    /**
     * This is the base of any command, e.g. /op <target>, op would be the base command.
     *
     * @param name The name of the command.
     * @param aliases An `Iterable` of name aliases.
     */
    constructor(name: String, aliases: Iterable<String>) : this(name, permissions = listOf(), aliases = aliases)
    /**
     * This is the base of any command, e.g. /op <target>, op would be the base command.
     *
     * @param name The name of the command.
     * @param permission A single Bukkit permission to be applied as a requirement.
     * @param aliases An `Iterable` of name aliases.
     */
    constructor(name: String, permission: String, aliases: Iterable<String> = listOf()) : this(name, listOf(permission), aliases)

    init {
        setDescription("A command made with Stellar.")
        addRequirements(*permissions.toList().toTypedArray())
        addAliases(*aliases.toList().toTypedArray())
    }

    override fun setInformation(name: String, text: String): StellarCommand = apply { information[name] = text }
    override fun setDescription(text: String): StellarCommand = setInformation("Description", text)
    override fun setUsageText(text: String): StellarCommand = setInformation("Usage", text)
    override fun clearInformation(): StellarCommand = apply { aliases.clear() }

    override fun register(plugin: JavaPlugin) = apply {
        if (aliases.isNotEmpty()) setInformation("Aliases", aliases.joinToString())
        NMSManager.register(this, plugin)
    }

}