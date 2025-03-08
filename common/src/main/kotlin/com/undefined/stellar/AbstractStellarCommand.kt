package com.undefined.stellar

import com.undefined.stellar.data.argument.CommandContext
import com.undefined.stellar.data.execution.StellarExecution
import com.undefined.stellar.data.execution.StellarRunnable
import com.undefined.stellar.data.failure.HideDefaultFailureMessages
import com.undefined.stellar.data.requirement.StellarRequirement
import com.undefined.stellar.nms.NMS
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

@Suppress("UNCHECKED_CAST")
abstract class AbstractStellarCommand<T : AbstractStellarCommand<T>>(val name: String) {

    lateinit var nms: NMS

    val aliases: MutableSet<String> = mutableSetOf()
    val requirements: MutableList<StellarRequirement<*>> = mutableListOf()
    val arguments: MutableSet<AbstractStellarArgument<*, *>> = mutableSetOf()
    val executions: MutableSet<StellarExecution<*>> = mutableSetOf()
    val runnables: MutableSet<StellarRunnable<*>> = mutableSetOf()
    val failureExecutions: MutableSet<StellarExecution<*>> = mutableSetOf()
    open val globalFailureExecutions: MutableSet<StellarExecution<*>> = mutableSetOf()
    var hideDefaultFailureMessages: HideDefaultFailureMessages = HideDefaultFailureMessages(hide = false, global = false)

    fun addAlias(alias: String): T = apply { aliases.add(alias) } as T
    fun addAliases(vararg alias: String): T = apply { aliases.addAll(alias) } as T
    fun clearAliases(): T = apply { aliases.clear() } as T

    fun <T : AbstractStellarArgument<*, *>> addArgument(argument: T): T = argument.apply {
        argument.parent = this@AbstractStellarCommand
        this@AbstractStellarCommand.arguments.add(argument)
    }

    inline fun <reified C : CommandSender> addRequirement(noinline requirement: C.() -> Boolean): T = apply {
        requirements.add(StellarRequirement(C::class, requirement))
    } as T

    fun addRequirement(permission: String): T = addRequirement<CommandSender> { hasPermission(permission) }

    /**
     * Note: this only applies to players, not all command senders.
     */
    fun addRequirement(level: Int): T = addRequirement<Player> { nms.hasPermission(this, level) }

    fun addRequirements(vararg permissions: String): T = addRequirement<CommandSender> { permissions.all { hasPermission(it) } }

    /**
     * Note: this only applies to players, not all command senders.
     */
    fun addRequirements(vararg levels: Int): T = addRequirement<Player> { levels.all { nms.hasPermission(this, it) } }

    inline fun <reified C : CommandSender> addExecution(noinline execution: CommandContext<C>.() -> Unit): T = apply {
        executions.add(StellarExecution(C::class, execution, false))
    } as T

    inline fun <reified C : CommandSender> addAsyncExecution(noinline execution: CommandContext<C>.() -> Unit): T = apply {
        executions.add(StellarExecution(C::class, execution, true))
    } as T

    inline fun <reified C : CommandSender> addRunnable(noinline runnable: CommandContext<C>.() -> Boolean): T = apply {
        runnables.add(StellarRunnable(C::class, runnable, false))
    } as T

    inline fun <reified C : CommandSender> addAsyncRunnable(noinline runnable: CommandContext<C>.() -> Boolean): T = apply {
        runnables.add(StellarRunnable(C::class, runnable, true))
    } as T

    inline fun <reified C : CommandSender> addFailureExecution(noinline execution: CommandContext<C>.() -> Unit): T = apply {
        failureExecutions.add(StellarExecution(C::class, execution, false))
    } as T

    inline fun <reified C : CommandSender> addGlobalFailureExecution(noinline execution: CommandContext<C>.() -> Unit): T = apply {
        globalFailureExecutions.add(StellarExecution(C::class, execution, false))
    } as T

    fun addFailureMessage(message: Component): T = apply {
        failureExecutions.add(StellarExecution(CommandSender::class, { sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().serialize(message)) }, false))
    } as T

    fun addGlobalFailureMessage(message: Component): T = apply {
        globalFailureExecutions.add(StellarExecution(CommandSender::class, { sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().serialize(message)) }, false))
    } as T

    fun addFailureMessage(message: String): T = apply {
        val component = MiniMessage.miniMessage().deserialize(message)
        failureExecutions.add(StellarExecution(CommandSender::class, { sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().serialize(component)) }, false))
    } as T

    fun addGlobalFailureMessage(message: String): T = apply {
        val component = MiniMessage.miniMessage().deserialize(message)
        globalFailureExecutions.add(StellarExecution(CommandSender::class, { sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().serialize(component)) }, false))
    } as T

    fun hideDefaultFailureMessages(hide: Boolean = true, global: Boolean = false): T = apply {
        hideDefaultFailureMessages = HideDefaultFailureMessages(hide, global)
    } as T

    open fun hasGlobalHiddenDefaultFailureMessages(): Boolean = hideDefaultFailureMessages.hide && hideDefaultFailureMessages.global

    abstract fun setInformation(name: String, text: String): T
    abstract fun setDescription(text: String): T
    abstract fun setUsageText(text: String): T
    abstract fun clearInformation(): T

    abstract fun register(plugin: JavaPlugin): T

}