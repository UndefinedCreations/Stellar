package com.undefined.stellar

import com.undefined.stellar.data.HideDefaultFailureMessages
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.requirement.PermissionStellarRequirement
import com.undefined.stellar.data.execution.StellarExecution
import com.undefined.stellar.data.execution.StellarRunnable
import com.undefined.stellar.data.requirement.StellarRequirement
import com.undefined.stellar.sub.SubCommandHandler
import com.undefined.stellar.sub.custom.CustomSubCommand
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

@Suppress("UNCHECKED_CAST")
abstract class AbstractStellarCommand<T>(val name: String) : SubCommandHandler() {

    override fun getBase(): AbstractStellarCommand<*> = this

    val aliases: MutableList<String> = mutableListOf()
    var description: String = ""
    var usage: String = ""
    val failureMessages: MutableList<Component> = mutableListOf()
    val globalFailureMessages: MutableList<Component> = mutableListOf()
    val failureExecutions: MutableList<CustomStellarExecution<*, Any>> = mutableListOf()
    var hideDefaultFailureMessages: HideDefaultFailureMessages = HideDefaultFailureMessages(false, false)
    private val _requirements: MutableList<StellarRequirement<*>> = mutableListOf()
    val requirements: List<StellarRequirement<*>>
        get() {
            if (this is CustomSubCommand<*>)
                _requirements.addFirst(
                    StellarRequirement(CommandSender::class) { this@AbstractStellarCommand.requirement() }
                )
            return _requirements
        }
    val permissionRequirements: MutableList<PermissionStellarRequirement> = mutableListOf()
    val executions: MutableList<StellarExecution<*>> = mutableListOf()
    val runnables: MutableList<StellarRunnable<*>> = mutableListOf()

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
        addRequirement(StellarRequirement(C::class, requirement))
        return this as T
    }

    fun addRequirement(requirement: StellarRequirement<*>) {
        _requirements.add(requirement)
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

    @Suppress("UNCHECKED_CAST")
    inline fun <reified C : CommandSender> addFailureExecution(noinline execution: C.(String) -> Unit): T {
        failureExecutions.add(CustomStellarExecution(C::class, execution) as CustomStellarExecution<*, Any>)
        return this as T
    }

    fun hideDefaultFailureMessages(hide: Boolean = true, global: Boolean = false): T {
        hideDefaultFailureMessages = HideDefaultFailureMessages(hide, global)
        return this as T
    }

    inline fun <reified C : CommandSender> addExecution(noinline execution: C.() -> Unit): T {
        executions.add(StellarExecution(C::class, execution))
        return this as T
    }

    inline fun <reified C : CommandSender> alwaysRun(noinline execution: C.() -> Boolean): T {
        runnables.add(StellarRunnable(C::class, execution))
        return this as T
    }

    fun hasGlobalHiddenDefaultFailureMessages(): Boolean {
        val base = getBase()
        return base.hideDefaultFailureMessages.hide && base.hideDefaultFailureMessages.global
    }

    abstract fun register(plugin: JavaPlugin)

}