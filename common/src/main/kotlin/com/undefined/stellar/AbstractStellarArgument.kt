package com.undefined.stellar

import com.mojang.brigadier.arguments.ArgumentType
import com.undefined.stellar.data.argument.CommandContext
import com.undefined.stellar.data.execution.ExecutableExecution
import com.undefined.stellar.data.suggestion.*
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.ApiStatus
import java.util.concurrent.CompletableFuture

/**
 * Represents an argument, whether literal or parameter (non-literal).
 *
 * @param name The name of the argument.
 */
@Suppress("UNCHECKED_CAST")
abstract class AbstractStellarArgument<T : AbstractStellarArgument<T>>(name: String) : AbstractStellarCommand<T>(name) {

    /**
     * Represents the parent command, whether it's a command or an argument. This is automatically set internally.
     */
    open lateinit var parent: AbstractStellarCommand<*>

    override val globalFailureExecutions: MutableSet<ExecutableExecution<*>>
        get() = parent.globalFailureExecutions

    override fun hasGlobalHiddenDefaultFailureMessages(): Boolean = parent.hasGlobalHiddenDefaultFailureMessages()

    override fun setInformation(name: String, text: String): T = apply { parent.setInformation(name, text) } as T
    override fun setDescription(text: String): T = apply { parent.setDescription(text) } as T
    override fun setUsageText(text: String): T = apply { parent.setUsageText(text) } as T
    override fun clearInformation(): T = apply { parent.clearInformation() } as T

    override fun register(plugin: JavaPlugin, prefix: String): T = apply { parent.register(plugin, prefix) } as T

}