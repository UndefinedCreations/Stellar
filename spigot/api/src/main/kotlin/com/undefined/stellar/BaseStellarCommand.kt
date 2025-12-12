package com.undefined.stellar

import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.ApiStatus

/**
 * This is an abstract class allowing you to create a command by extending a class.
 *
 * @property name The primary name of the command.
 * @property permission A [String] representing the required permission to execute the command (optional).
 * @property aliases A [List] of command aliases (optional).
 */
@ApiStatus.Experimental
abstract class BaseStellarCommand(val name: String, val permission: String = "", val aliases: List<String> = listOf()) {

    private var hasBeenRegistered = false
    private val command: StellarCommandImpl by lazy {
        setup().apply { for (argument in arguments()) addArgument(argument.fullArgument) }
    }

    /**
     * Sets up and returns the core [StellarCommandImpl] instance for this command.
     * Called once during command initialization.
     */
    abstract fun setup(): StellarCommandImpl

    /**
     * Optionally provides additional [StellarArgument]s to be added to the command.
     *
     * @return A list of additional [StellarArgument]s.
     */
    open fun arguments(): List<StellarArgument> = listOf()

    /**
     * Creates a new [StellarCommandImpl] instance with the given configuration block.
     *
     * Usually, this is used along with the [setup] method as such:
     * ```kotlin
     * override fun setup() = createCommand {
     *   // code logic
     * }
     * ```
     *
     * @param init A lambda to configure the command instance.
     * @return The configured [StellarCommandImpl].
     */
    open fun createCommand(init: StellarCommandImpl.() -> Unit): StellarCommandImpl {
        val command = StellarCommandImpl(name, permission, aliases)
        command.init()
        return command
    }

    /**
     * Registers the command with the given plugin, if it has not been registered yet.
     *
     * @param plugin The [JavaPlugin] instance used for registration (default: plugin in [StellarConfig]).
     */
    fun register(plugin: JavaPlugin = StellarConfig.plugin ?: throw IllegalArgumentException("Plugin cannot be null!")) {
        if (hasBeenRegistered) return
        hasBeenRegistered = true
        command.register(plugin)
    }

}