package com.undefined.stellar.sub.brigadier.primitive

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.brigadier.BrigadierTypeSubCommand
import org.bukkit.command.CommandSender

@Suppress("UNCHECKED_CAST")
class DoubleSubCommand(parent: AbstractStellarCommand<*>, name: String, val min: Double, val max: Double) : BrigadierTypeSubCommand<DoubleSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addDoubleExecution(noinline execution: T.(Double) -> Unit): DoubleSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunDouble(noinline execution: T.(Double) -> Boolean): DoubleSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any>)
        return this
    }
}
