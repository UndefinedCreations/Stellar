package com.undefined.stellar.sub.brigadier.primitive

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.brigadier.BrigadierTypeSubCommand
import org.bukkit.command.CommandSender

@Suppress("UNCHECKED_CAST")
class IntegerSubCommand(parent: BaseStellarCommand<*>, name: String, val min: Int, val max: Int) : BrigadierTypeSubCommand<IntegerSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addIntegerExecution(noinline execution: T.(Int) -> Unit): IntegerSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunInteger(noinline execution: T.(Int) -> Boolean): IntegerSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any>)
        return this
    }
}
