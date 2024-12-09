package com.undefined.stellar

import com.undefined.stellar.argument.ArgumentHandler
import com.undefined.stellar.data.argument.CommandContext
import com.undefined.stellar.data.execution.StellarExecution
import com.undefined.stellar.data.execution.StellarRunnable
import com.undefined.stellar.data.failure.HideDefaultFailureMessages
import com.undefined.stellar.data.requirement.PermissionStellarRequirement
import com.undefined.stellar.data.requirement.StellarRequirement
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.ApiStatus

@Suppress("UNCHECKED_CAST")
abstract class AbstractStellarCommand<T>(val name: String, var description: String = "", var usage: String = "") : ArgumentHandler() {

    override val base: AbstractStellarCommand<*>
        get() = this
    @ApiStatus.Internal val aliases: MutableList<String> = mutableListOf()
    @ApiStatus.Internal open val failureMessages: MutableList<Component> = mutableListOf()
    @ApiStatus.Internal open val globalFailureMessages: MutableList<Component> = mutableListOf()
    @ApiStatus.Internal open val failureExecutions: MutableList<StellarExecution<*>> = mutableListOf()
    @ApiStatus.Internal open var hideDefaultFailureMessages: HideDefaultFailureMessages = HideDefaultFailureMessages(false, false)
    @ApiStatus.Internal open val requirements: MutableList<StellarRequirement<*>> = mutableListOf()
    @ApiStatus.Internal open val permissionRequirements: MutableList<PermissionStellarRequirement> = mutableListOf()
    @ApiStatus.Internal open val executions: MutableList<StellarExecution<*>> = mutableListOf()
    @ApiStatus.Internal open val runnables: MutableList<StellarRunnable<*>> = mutableListOf()

    fun addAlias(name: String): T {
        aliases.add(name)
        return this as T
    }

    fun setDescription(description: String): T {
        this.description = description
        return this as T
    }

    fun setUsageText(text: String): T {
        usage = text
        return this as T
    }

    fun addRequirement(permission: String): T {
        permissionRequirements.add(PermissionStellarRequirement(1, permission))
        return this as T
    }

    fun addRequirement(permissionLevel: Int): T {
        permissionRequirements.add(PermissionStellarRequirement(permissionLevel))
        return this as T
    }

    inline fun <reified C : CommandSender> addRequirement(noinline requirement: C.() -> Boolean): T {
        addRequirement(StellarRequirement(requirement))
        return this as T
    }

    fun addRequirement(requirement: StellarRequirement<*>): T {
        requirements.add(requirement)
        return this as T
    }

    fun addFailureMessage(message: String): T {
        failureMessages.add(MiniMessage.miniMessage().deserialize(message))
        return this as T
    }

    fun addFailureMessage(message: Component): T {
        failureMessages.add(message)
        return this as T
    }

    fun addGlobalFailureMessage(message: String): T {
        globalFailureMessages.add(MiniMessage.miniMessage().deserialize(message))
        return this as T
    }

    fun addGlobalFailureMessage(message: Component): T {
        globalFailureMessages.add(message)
        return this as T
    }

    inline fun <reified C : CommandSender> addFailureExecution(noinline execution: CommandContext<C>.() -> Unit): T {
        failureExecutions.add(StellarExecution(execution))
        return this as T
    }

    fun hideDefaultFailureMessages(hide: Boolean = true, global: Boolean = false): T {
        hideDefaultFailureMessages = HideDefaultFailureMessages(hide, global)
        return this as T
    }

    inline fun <reified C : CommandSender> setExecution(noinline execution: CommandContext<C>.() -> Unit): T {
        executions.clear()
        return addExecution<C>(execution)
    }

    inline fun <reified C : CommandSender> addExecution(noinline execution: CommandContext<C>.() -> Unit): T {
        executions.add(StellarExecution(execution))
        return this as T
    }

    inline fun <reified C : CommandSender> setRunnable(noinline runnable: CommandContext<C>.() -> Boolean): T {
        runnables.clear()
        return addRunnable<C>(runnable)
    }

    inline fun <reified C : CommandSender> addRunnable(noinline runnable: CommandContext<C>.() -> Boolean): T {
        runnables.add(StellarRunnable(runnable))
        return this as T
    }

    fun hasGlobalHiddenDefaultFailureMessages(): Boolean =
        base.hideDefaultFailureMessages.hide && base.hideDefaultFailureMessages.global

    abstract fun register(plugin: JavaPlugin)

}