package com.undefined.stellar.sub.brigadier.math

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.data.arguments.Operation
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.brigadier.BrigadierTypeSubCommand
import org.bukkit.command.CommandSender

@Suppress("UNCHECKED_CAST")
class OperationSubCommand(parent: AbstractStellarCommand<*>, name: String) : BrigadierTypeSubCommand<OperationSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addOperationExecution(noinline execution: T.(Operation) -> Unit): OperationSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunOperation(noinline execution: T.(Operation) -> Boolean): OperationSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any>)
        return this
    }
}