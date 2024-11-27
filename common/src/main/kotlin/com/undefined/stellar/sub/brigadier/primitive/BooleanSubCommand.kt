package com.undefined.stellar.sub.brigadier.primitive

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.BaseStellarSubCommand
import org.bukkit.command.CommandSender

@Suppress("UNCHECKED_CAST")
class BooleanSubCommand(parent: AbstractStellarCommand<*>, name: String) : BaseStellarSubCommand<BooleanSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addBooleanExecution(noinline execution: T.(Boolean) -> Unit): BooleanSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any?>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunBoolean(noinline execution: T.(Boolean) -> Boolean): BooleanSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any?>)
        return this
    }
}
