package com.undefined.stellar

import com.undefined.stellar.argument.ArgumentHandler
import com.undefined.stellar.data.argument.CommandContext
import com.undefined.stellar.data.execution.StellarExecution
import com.undefined.stellar.data.execution.StellarRunnable
import com.undefined.stellar.data.failure.HideDefaultFailureMessages
import com.undefined.stellar.data.help.CustomCommandHelpTopic
import com.undefined.stellar.data.requirement.PermissionStellarRequirement
import com.undefined.stellar.data.requirement.StellarRequirement
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.ApiStatus
import java.util.*
import kotlin.collections.HashMap

@Suppress("UNCHECKED_CAST")
abstract class AbstractStellarCommand<T>(val name: String, val description: String = "", usage: String = "") : ArgumentHandler() {

    override val base: AbstractStellarCommand<*>
        get() = this
    @ApiStatus.Internal val aliases: MutableList<String> = mutableListOf()
    @ApiStatus.Internal val helpTopic: SortedMap<String, String> = sortedMapOf()
    @ApiStatus.Internal open val failureMessages: MutableList<Component> = mutableListOf()
    @ApiStatus.Internal open val globalFailureMessages: MutableList<Component> = mutableListOf()
    @ApiStatus.Internal open val failureExecutions: MutableList<StellarExecution<*>> = mutableListOf()
    @ApiStatus.Internal open var hideDefaultFailureMessages: HideDefaultFailureMessages = HideDefaultFailureMessages(false, false)
    @ApiStatus.Internal open val requirements: MutableList<StellarRequirement<*>> = mutableListOf()
    @ApiStatus.Internal open val permissionRequirements: MutableList<PermissionStellarRequirement> = mutableListOf()
    @ApiStatus.Internal open val executions: MutableList<StellarExecution<*>> = mutableListOf()
    @ApiStatus.Internal open val runnables: MutableList<StellarRunnable<*>> = mutableListOf()
    @ApiStatus.Internal open val registerExecutions: MutableList<() -> Unit> = mutableListOf()

    init {
        if (description.isNotEmpty()) helpTopic["Description"] to description
        if (usage.isNotEmpty()) helpTopic["Usage"] to usage
        if (aliases.isNotEmpty()) helpTopic["Aliases"] = aliases.joinToString(", ")
    }

    fun setAliases(vararg aliases: String): T {
        this.aliases.clear()
        for (alias in aliases) this.aliases.add(alias)
        return this as T
    }

    fun addAliases(vararg aliases: String): T {
        for (alias in aliases) this.aliases.add(alias)
        return this as T
    }

    fun setDescription(description: String): T {
        helpTopic["Description"] = description
        return this as T
    }

    fun setUsageText(usage: String): T {
        helpTopic["Usage"] = usage
        return this as T
    }

    fun setHelpTopicInformation(name: String, text: String): T {
        helpTopic[name] = text
        return this as T
    }

    fun clearHelpTopicInformation(): T {
        helpTopic.clear()
        return this as T
    }

    fun setRequirements(permissions: List<StellarRequirement<*>>): T {
        this.requirements.clear()
        return addRequirements(permissions)
    }

    fun setPermissionRequirements(permissions: List<PermissionStellarRequirement>): T {
        permissionRequirements.clear()
        return addPermissionRequirements(permissions)
    }

    fun setRequirements(vararg permissions: StellarRequirement<*>): T {
        this.requirements.clear()
        return addRequirements(*permissions)
    }

    fun setRequirements(vararg permissions: PermissionStellarRequirement): T {
        permissionRequirements.clear()
        return addRequirements(*permissions)
    }

    fun addRequirements(permissions: List<StellarRequirement<*>>): T {
        for (permission in permissions) this.requirements.add(permission)
        return this as T
    }

    fun addPermissionRequirements(permissions: List<PermissionStellarRequirement>): T {
        for (permission in permissions) permissionRequirements.add(permission)
        return this as T
    }

    fun addRequirements(vararg permissions: StellarRequirement<*>): T =
        addRequirements(permissions.toList())

    fun addRequirements(vararg permissions: PermissionStellarRequirement): T =
        addPermissionRequirements(permissions.toList())

    fun addRequirements(vararg levels: Int): T {
        for (level in levels) permissionRequirements.add(PermissionStellarRequirement(level))
        return this as T
    }

    fun addRequirements(vararg permissions: String): T {
        for (permission in permissions) permissionRequirements.add(PermissionStellarRequirement(1, permission))
        return this as T
    }

    inline fun <reified C : CommandSender> addRequirement(noinline requirement: C.() -> Boolean): T =
        addRequirements(StellarRequirement(C::class, requirement))

    fun setFailureMessages(messages: List<Component>): T {
        failureMessages.clear()
        return addFailureMessages(messages)
    }

    fun setFailureMessages(vararg messages: Component): T =
        setFailureMessages(messages.toList())

    fun setFailureMessages(vararg messages: String): T =
        setFailureMessages(messages.map { MiniMessage.miniMessage().deserialize(it) }.toList())

    fun setPlainFailureMessages(messages: List<String>): T {
        failureMessages.clear()
        return addFailureMessages(messages.map { Component.text(it) })
    }

    fun setPlainFailureMessages(vararg messages: String): T =
        setFailureMessages(messages.map { Component.text(it) }.toList())

    fun setGlobalFailureMessages(messages: List<Component>): T {
        globalFailureMessages.clear()
        for (message in messages) globalFailureMessages.add(message)
        return this as T
    }

    fun setGlobalFailureMessages(vararg messages: String): T =
        setGlobalFailureMessages(messages.map { MiniMessage.miniMessage().deserialize(it) }.toList())

    fun setGlobalFailureMessages(vararg messages: Component): T =
        setGlobalFailureMessages(messages.toList())

    fun addFailureMessages(messages: List<Component>): T {
        for (message in messages) failureMessages.add(message)
        return this as T
    }

    fun addFailureMessages(vararg messages: String): T =
        addFailureMessages(messages.map { MiniMessage.miniMessage().deserialize(it) }.toList())

    fun addFailureMessages(vararg messages: Component): T =
        addFailureMessages(messages.toList())

    fun addPlainFailureMessages(messages: List<String>): T =
        addFailureMessages(messages.map { Component.text(it) })

    fun addPlainFailureMessages(vararg messages: String): T =
        addPlainFailureMessages(messages.toList())

    fun addGlobalFailureMessages(messages: List<Component>): T {
        for (message in messages) globalFailureMessages.add(message)
        return this as T
    }

    fun addGlobalFailureMessages(vararg messages: String): T =
        addGlobalFailureMessages(messages.map { MiniMessage.miniMessage().deserialize(it) }.toList())

    fun addGlobalFailureMessages(vararg messages: Component): T =
        addGlobalFailureMessages(messages.toList())

    inline fun <reified C : CommandSender> addFailureExecution(noinline execution: CommandContext<C>.() -> Unit): T {
        failureExecutions.add(StellarExecution(C::class, execution))
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
        executions.add(StellarExecution(C::class, execution))
        return this as T
    }

    inline fun <reified C : CommandSender> setRunnable(noinline runnable: CommandContext<C>.() -> Boolean): T {
        runnables.clear()
        return addRunnable<C>(runnable)
    }

    inline fun <reified C : CommandSender> addRunnable(noinline runnable: CommandContext<C>.() -> Boolean): T {
        runnables.add(StellarRunnable(C::class, runnable))
        return this as T
    }

    fun onRegister(execution: () -> Unit): T {
        base.registerExecutions.add(execution)
        return this as T
    }

    fun hasGlobalHiddenDefaultFailureMessages(): Boolean =
        base.hideDefaultFailureMessages.hide && base.hideDefaultFailureMessages.global

    abstract fun register(plugin: JavaPlugin)

}