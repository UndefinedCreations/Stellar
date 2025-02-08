package com.undefined.stellar.argument.basic

import com.undefined.stellar.argument.AbstractStellarArgument
import com.undefined.stellar.data.argument.CommandContext
import com.undefined.stellar.data.execution.StellarExecution
import com.undefined.stellar.data.execution.StellarRunnable
import com.undefined.stellar.data.requirement.StellarRequirement
import com.undefined.stellar.data.suggestion.StellarSuggestion
import com.undefined.stellar.data.suggestion.Suggestion
import org.bukkit.command.CommandSender
import org.jetbrains.annotations.ApiStatus
import java.util.concurrent.CompletableFuture

abstract class CustomArgument<R, T>(
    val type: AbstractStellarArgument<*, R>
) : AbstractStellarArgument<CustomArgument<*, T>, R>(type.parent, type.name) {

    override val arguments: MutableList<AbstractStellarArgument<*, *>>
        get() = (super.arguments + getArgumentsList()).toMutableList()
    override val failureExecutions: MutableList<StellarExecution<*>>
        get() = (super.failureExecutions + StellarExecution(CommandSender::class) {
            failureExecution(this, arguments.values.last())
        }).toMutableList()
    override val requirements: MutableList<StellarRequirement<*>>
        get() = (super.requirements + StellarRequirement(CommandSender::class) { requirement() }).toMutableList()
    override val executions: MutableList<StellarExecution<*>>
        get() = (super.executions + StellarExecution(CommandSender::class) {
            execution(this, this.arguments.values.last())
        }).toMutableList()
    override val runnables: MutableList<StellarRunnable<*>>
        get() = (super.runnables + StellarRunnable(CommandSender::class) {
            runnable(this, this[name])
        }).toMutableList()
    override val suggestions: MutableList<StellarSuggestion<*>>
        get() = (super.suggestions + StellarSuggestion(CommandSender::class) {
            listSuggestions(this)
        }).toMutableList()

    @Suppress("UNCHECKED_CAST")
    @ApiStatus.Internal fun parseInternal(context: CommandContext<CommandSender>, value: Any?) = parse(context, value as R)

    open fun getArgumentsList(): Collection<AbstractStellarArgument<*, *>> = listOf()
    abstract fun parse(context: CommandContext<CommandSender>, value: R): T
    open fun <T> execution(context: CommandContext<CommandSender>, value: T) {}
    open fun <T> runnable(context: CommandContext<CommandSender>, value: T): Boolean = true
    open fun <T> failureExecution(context: CommandContext<CommandSender>, value: T) {}
    open fun requirement(): Boolean = true
    open fun listSuggestions(context: CommandContext<CommandSender>): CompletableFuture<Collection<Suggestion>> = CompletableFuture.completedFuture(listOf())

}