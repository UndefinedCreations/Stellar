package com.undefined.stellar.command

import com.undefined.stellar.argument.AbstractStellarArgument
import com.undefined.stellar.data.argument.CommandContext
import com.undefined.stellar.data.execution.StellarExecution
import com.undefined.stellar.data.execution.StellarRunnable
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

@Suppress("UNCHECKED_CAST")
abstract class AbstractStellarCommand<T : AbstractStellarCommand<T>>(val name: String) {

    val arguments: MutableList<AbstractStellarArgument<*, *>> = mutableListOf()
    val executions: MutableList<StellarExecution<*>> = mutableListOf()

    fun <T : AbstractStellarArgument<T, *>> addArgument(argument: T): T = argument.apply {
        argument.parent = this@AbstractStellarCommand
        this@AbstractStellarCommand.arguments.add(argument)
    }

    inline fun <reified C : CommandSender> addExecution(noinline execution: CommandContext<C>.() -> Unit): T = apply {
        executions.add(StellarExecution(C::class, execution, false))
    } as T

    inline fun <reified C : CommandSender> addAsyncExecution(noinline execution: CommandContext<C>.() -> Unit): T = apply {
        executions.add(StellarExecution(C::class, execution, true))
    } as T

    abstract fun register(plugin: JavaPlugin): T

}