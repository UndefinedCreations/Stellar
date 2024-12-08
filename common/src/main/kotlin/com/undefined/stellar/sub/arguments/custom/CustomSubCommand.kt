package com.undefined.stellar.sub.arguments.custom

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.data.argument.CommandContext
import com.undefined.stellar.data.execution.StellarExecution
import com.undefined.stellar.data.execution.StellarRunnable
import com.undefined.stellar.data.requirement.StellarRequirement
import com.undefined.stellar.data.suggestion.StellarSuggestion
import com.undefined.stellar.data.suggestion.Suggestion
import com.undefined.stellar.sub.AbstractStellarSubCommand
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

abstract class CustomSubCommand<T>(
    parent: AbstractStellarCommand<*>,
    name: String,
    val type: AbstractStellarSubCommand<*>
) : AbstractStellarSubCommand<T>(parent, name) {

    override val failureExecutions: MutableList<StellarExecution<*>>
        get() = (super.failureExecutions + StellarExecution {
            failureExecution(this, arguments.values.last())
        }).toMutableList()
    override val requirements: MutableList<StellarRequirement<*>>
        get() = (super.requirements + StellarRequirement<CommandSender> { requirement() }).toMutableList()
    override val executions: MutableList<StellarExecution<*>>
        get() = (super.executions + StellarExecution {
            execution(this, this.arguments.values.last())
        }).toMutableList()
    override val runnables: MutableList<StellarRunnable<*>>
        get() = (super.runnables + StellarRunnable {
            runnable(this, this[name])
        }).toMutableList()
    override val suggestions: MutableList<StellarSuggestion<*>>
        get() = (super.suggestions + StellarSuggestion {
            listSuggestions(this)
        }).toMutableList()

    open fun getSubCommandsList(): List<AbstractStellarSubCommand<*>> = listOf()
    abstract fun parse(context: CommandContext<CommandSender>): T
    open fun <T> execution(info: CommandContext<CommandSender>, value: T) {}
    open fun <T> runnable(info: CommandContext<CommandSender>, value: T): Boolean = true
    open fun <T> failureExecution(info: CommandContext<CommandSender>, value: T) {}
    open fun requirement(): Boolean = true
    abstract fun listSuggestions(context: CommandContext<CommandSender>): List<Suggestion>

    override fun getBase(): AbstractStellarCommand<*> = parent.getBase()
    override fun register(plugin: JavaPlugin) = parent.register(plugin)
}